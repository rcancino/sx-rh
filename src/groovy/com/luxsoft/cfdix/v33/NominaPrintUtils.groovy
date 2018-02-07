package com.luxsoft.cfdix.v33

import org.apache.commons.logging.LogFactory

import com.luxsoft.sw4.rh.Empleado
import com.luxsoft.sw4.rh.NominaPorEmpleado
import com.luxsoft.sw4.Empresa
import com.luxsoft.sw4.rh.TiempoExtra
import com.luxsoft.sw4.rh.Finiquito
import com.luxsoft.sw4.rh.CalculoAnual
import com.luxsoft.sw4.MonedaUtils

import lx.cfdi.utils.DateUtils
import lx.cfdi.v33.ObjectFactory
import lx.cfdi.v33.Comprobante
import com.luxsoft.cfdix.CfdiTimbre
import com.luxsoft.sw4.cfdi.ImporteALetra

import lx.cfdi.v33.nomina.Nomina
import lx.cfdi.v33.nomina.NominaUtils

import javax.imageio.ImageIO;
import net.glxn.qrgen.QRCode
import net.glxn.qrgen.image.ImageType
import java.text.MessageFormat;
import lx.cfdi.v33.CfdiUtils
import com.luxsoft.sw4.cfdi.Cfdi
import org.apache.commons.lang.exception.ExceptionUtils



class NominaPrintUtils {

	private static final log = LogFactory.getLog(this)

    static getParametros(Cfdi cfdi, Nomina nomina, NominaPorEmpleado nominaPorEmpleado){
        
        def parametros=[:]
        Comprobante comprobante = CfdiUtils.read(cfdi.xml)
        Empresa empresa = Empresa.first()
        Comprobante.Emisor  emisor=comprobante.getEmisor();
        parametros.put("EMISOR_NOMBRE",     emisor.getNombre());
        parametros.put("EMISOR_RFC",        emisor.getRfc());
        parametros.put("EMISOR_DIRECCION", empresa.direccion.codigoPostal);
        parametros.put("EXPEDIDO_EN", empresa.direccion.codigoPostal);
        parametros.put("REGIMEN",comprobante.getEmisor().regimenFiscal);
        parametros.put("METODO_DE_PAGO",comprobante.metodoPago.toString());

        parametros.put("NOMBRE", comprobante.getReceptor().getNombre()); 
        parametros.put("RFC", comprobante.getReceptor().getRfc());
        parametros['NUMERO_EMPLEADO'] = nomina.receptor.numEmpleado
        parametros['NUMERO_IMSS'] = nomina.receptor.numSeguridadSocial

        parametros['CURP'] = nomina.receptor.curp
        parametros['REGIMEN_TRABAJADOR'] = nomina.receptor.tipoRegimen
        parametros['EMISOR_RFC'] = comprobante.emisor.rfc
        parametros['PERIOCIDAD_PAGO'] = nomina.receptor.periodicidadPago
        parametros.put("SERIE", comprobante.getSerie());
        parametros.put("FOLIO", comprobante.getFolio());
        parametros.put("NUM_CERTIFICADO",   comprobante.getNoCertificado());
        parametros.put("SELLO_DIGITAL",     comprobante.getSello());
        parametros.put("IMP_CON_LETRA",     ImporteALetra.aLetra(comprobante.getTotal()));
        
        if(nominaPorEmpleado.nomina.tipo!='ASIMILADOS'){
            parametros['REGISTRO_PATRONAL']=nomina.emisor.registroPatronal
            parametros['RIESGO_PUESTO']=''+nomina.receptor.riesgoPuesto
            parametros['TIPO_JORNADA'] = nomina.receptor.tipoJornada
            parametros['FECHA_INGRESO_LABORAL'] = nomina.receptor?.fechaInicioRelLaboral
            parametros['TIPO_CONTRATO'] = nomina.receptor.tipoContrato
            parametros['FECHA_INICIAL'] = nomina.fechaInicialPago
            parametros['FECHA_FINAL'] = nomina.fechaFinalPago
            parametros['DIAS_PAGADOS'] = nomina.numDiasPagados
            parametros['ASIMILADOS']='NO'
            parametros['BANCO'] = nominaPorEmpleado.nomina.formaDePago == 'TRANSFERENCIA'? nomina.receptor.banco?.toString() : ''
            parametros['CLABE'] = nomina.receptor.cuentaBancaria?.toString()
        }else {
            parametros['REGISTRO_PATRONAL'] = ''
            parametros['RIESGO_PUESTO'] = ''
            parametros['TIPO_JORNADA'] = ''
            parametros['ANTIGUEDAD'] = ''
            parametros['ASIMILADOS']='SI'
            parametros['BANCO'] = ''
            parametros['CLABE']=''
        }
        
        parametros.put("NFISCAL", comprobante.getSerie()+": "+nominaPorEmpleado.nomina.folio+" - "+comprobante.getFolio())
        parametros['FECHA_INGRESO_LABORAL'] = nomina.receptor.fechaInicioRelLaboral
        parametros['TIPO_CONTRATO'] = nomina.receptor.tipoContrato.toString()
        parametros['FECHA_INICIAL'] = nomina.fechaInicialPago
        parametros['FECHA_FINAL'] = nomina.fechaFinalPago
        parametros['TOTAL']=comprobante.getTotal() //as String
        parametros['COMENTARIO_NOM']='Nómina'

        CfdiTimbre timbre = new CfdiTimbre(cfdi)

        def img = generarQR(comprobante, timbre)
        parametros.put("QR_CODE",img)
        
        parametros.put("FECHA_TIMBRADO", timbre.fechaTimbrado);
        parametros.put("FOLIO_FISCAL", timbre.uuid);
        parametros.put("SELLO_DIGITAL_SAT", timbre.selloSAT);
        parametros.put("CERTIFICADO_SAT", timbre.noCertificadoSAT);
        parametros.put("CADENA_ORIGINAL_SAT", timbre.cadenaOriginal());
        parametros['SALARIO_DIARIO_BASE'] = nomina.receptor.salarioBaseCotApor as String
        parametros['SALARIO_DIARIO_INTEGRADO'] = nomina.receptor.salarioDiarioIntegrado as String
        parametros['PUESTO'] = nomina.receptor.puesto
        parametros['DEPARTAMENTO'] = nomina.receptor.departamento
        parametros['TIPO_NOMINA']=nomina.tipoNomina.toString()
        parametros['SUCURSAL']=nominaPorEmpleado.empleado.perfil.ubicacion.clave
        parametros['DEPARTAMENTO']=nomina.receptor.departamento
        parametros['FECHA']= comprobante.fecha

        if(nominaPorEmpleado.asistencia){
            def diasTrabajados = 0
            def faltas=0
            if(nominaPorEmpleado){
                if(nominaPorEmpleado.asistencia){
                    if(!nominaPorEmpleado.empleado.controlDeAsistencia){
                        diasTrabajados=nominaPorEmpleado.diasTrabajados+nominaPorEmpleado.vacaciones+nominaPorEmpleado.asistencia.paternidad
                        faltas= (nominaPorEmpleado.asistencia.faltasManuales+(nominaPorEmpleado.asistencia.faltasManuales*0.167)+nominaPorEmpleado.incapacidades)
                    }else{
                        if(nominaPorEmpleado.empleado.alta<=nominaPorEmpleado.asistencia.calendarioDet.inicio){
                            diasTrabajados=nominaPorEmpleado.diasTrabajados+nominaPorEmpleado.vacaciones+nominaPorEmpleado.asistencia.paternidad
                            faltas=nominaPorEmpleado.diasDelPeriodo-nominaPorEmpleado.diasTrabajados-nominaPorEmpleado.vacaciones-nominaPorEmpleado.asistencia.paternidad
                        }else{
                            diasTrabajados=nominaPorEmpleado.diasTrabajados //-(nominaPorEmpleado.asistencia.faltasManuales+nominaPorEmpleado.incapacidades)
                            faltas=nominaPorEmpleado.faltas+nominaPorEmpleado.fraccionDescanso+nominaPorEmpleado.incapacidades  //(
                        }
                    }
                }           
            }
            parametros['FALTAS']=com.luxsoft.sw4.MonedaUtils.round(faltas,2) as String
            parametros['DIAS_TRABAJADOS'] = com.luxsoft.sw4.MonedaUtils.round(diasTrabajados,2) as String
        }   
        parametros['SUB_EMPLEO_APLIC']=nominaPorEmpleado.subsidioEmpleoAplicado

        def horasExtras = nominaPorEmpleado.conceptos.find {it.concepto.clave == 'P022'|| it.concepto.clave=='P023'}
        if(horasExtras) {
            def diasDobles = 0;
            def diasTriples = 0;
            def horasDobles = 0;
            def horasTriples = 0;
            def importeDobles = 0
            def importeTriples = 0
            nomina.getPercepciones().getPercepcionArray().each {
                it.getHorasExtraArray().each { pp->
                    if(pp.tipoHoras.toString() == '01') {
                        diasDobles=pp.getDias()
                        horasDobles= pp.getHorasExtra() 
                        importeDobles= pp.importePagado
                    }
                    if( pp.tipoHoras.toString() =='02'){
                        diasTriples=pp.getDias()    
                        horasTriples= pp.getHorasExtra()    
                        importeTriples = pp.importePagado
                    }


                }
            }
        
            parametros.put("DIAS_HORAS_EXTRA_DOBLE", diasDobles )
            parametros['TIPO_HORAS_DOBLE'] = '01'
            parametros['HORAS_EXTRA_DOBLE'] = horasDobles
            parametros['IMPORTE_HORAS_EXTRA_DOBLE'] = importeDobles

            if(importeTriples > 0 ) {   
                parametros['DIAS_HORAS_EXTRA_TRIPLE'] = diasDobles 
                parametros['TIPO_HORAS_TRIPLE'] ='02'
                parametros['HORAS_EXTRA_TRIPLE']= horasTriples
                parametros['IMPORTE_HORAS_EXTRA_TRIPLE'] = importeTriples
            }
        }

        // Parametros nuevos
        parametros['USO_CFDI'] = "P01"
        parametros['MONEDA'] = 'MXN'
        parametros['FECHA_PAGO'] = nomina.fechaPago
        parametros['SINDICALIZADO'] = nomina.receptor.sindicalizado
        parametros['ENTIDAD_FEDERATIVA'] = nomina.receptor.claveEntFed.toString()
        parametros['TIPO_DE_COMPROBANTE'] = 'N (Nónina)'
        parametros['ANTIGUEDAD'] = nomina.receptor.antigüedad

        // concepto
        def concepto1 = comprobante.conceptos.concepto.get(0)
        parametros['CLAVE_PROD_SAT'] = concepto1.claveProdServ
        parametros['UNIDAD_SAT'] = concepto1.claveUnidad
        parametros['DESCRIPCION'] = concepto1.descripcion
        parametros['CANTIDAD'] = concepto1.cantidad
        parametros['VALOR_UNITARIO'] = concepto1.valorUnitario
        parametros['IMPORTE'] = concepto1.importe
        parametros['DESCUENTO'] = concepto1.descuento
        parametros['FORMA_DE_PAGO'] = comprobante.formaPago

        return parametros
    }

    public static  generarQR(Comprobante comprobante, CfdiTimbre timbre) {
        try {
            String pattern = "?re={0}&rr={1}&tt={2,number,##########.######}&id,{3}"
            String qq = MessageFormat.format(pattern, comprobante.emisor.rfc, comprobante.receptor.rfc, comprobante.total, timbre.uuid)
            File file = QRCode.from(qq).to(ImageType.GIF).withSize(250, 250).file()
            return file.toURI().toURL()
            
        } catch (Exception e) {
            throw new RuntimeException(ExceptionUtils.getRootCauseMessage(e),e)
        }
        
    }

    
    
}
