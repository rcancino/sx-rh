package com.luxsoft.sw4.cfdi.nomina12

import java.text.SimpleDateFormat

import org.apache.xmlbeans.XmlDate
import org.apache.xmlbeans.XmlOptions
import org.apache.xmlbeans.XmlCursor

import javax.xml.namespace.QName
import mx.gob.sat.cfd.x3.ComprobanteDocument


class NominaUtils {

	/**
	 * [toISO8601 description]
	 * 
	 * @param  fecha [description]
	 * @return       [description]
	 */
	static Calendar toISO8601(Date fecha) {
		Calendar c=Calendar.getInstance();
		c.setTime(fecha)
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd")
		XmlDate xmlDate = XmlDate.Factory.newInstance()
		xmlDate.setStringValue(df.format(c.getTime()))
		return xmlDate.getCalendarValue()
	}

	/**
	 * Ajusta el documento para complir con el SAT en lo referente al xml del CFDI
	 * 
	 * @param  document [description]
	 * @return          [description]
	 */
	static  depurar(ComprobanteDocument document){
		XmlCursor cursor=document.newCursor()
		if(cursor.toFirstChild()){
			QName qname = new QName("http://www.w3.org/2001/XMLSchema-instance","schemaLocation","xsi")
			cursor.setAttributeText(
				new QName("http://www.w3.org/2001/XMLSchema-instance","schemaLocation","xsi")
				,"http://www.sat.gob.mx/cfd/3 http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv32.xsd http://www.sat.gob.mx/nomina12  http://www.sat.gob.mx/sitio_internet/cfd/nomina/nomina12.xsd" )
			cursor.toNextToken()
			cursor.insertNamespace("cfdi", "http://www.sat.gob.mx/cfd/3")
			cursor.toNextToken()
			cursor.insertNamespace("nomina12", "http://www.sat.gob.mx/nomina12")
		}
	}

	static getXmlOptions(){
		XmlOptions options = new XmlOptions()
    	options.setCharacterEncoding("UTF-8")
    	options.put( XmlOptions.SAVE_INNER )
    	options.put( XmlOptions.SAVE_PRETTY_PRINT )
    	options.put( XmlOptions.SAVE_AGGRESSIVE_NAMESPACES )
    	options.put( XmlOptions.SAVE_USE_DEFAULT_NAMESPACE )
    	options.put(XmlOptions.SAVE_NAMESPACES_FIRST)
    	return options
	}

	/**
	 * Verifica que una clave sea de sueldos y salarios o concepto asimilado a salarios
	 * de acuerdo al catalogo del SAT
	 * 
	 * @param  tipoPercepcion [description]
	 * @return                [description]
	 */
	static Boolean isSueldos(String tipoPercepcion){
		return !OTRAS_PERCEPCIONES.contains(tipoPercepcion)
	}


	/**
	 *  Claves de percepciones que NO son sueldos y salarios ni conceptos asimilados a salarios
	 */
	static OTRAS_PERCEPCIONES = [
		'022', // Prima por antiguedad
    	'023', // Pagos por separacion
		'025', // Indemnizaciones
		'039', // Jubilaciones, pensiones o haberes de retiro en una exhibicion
		'044'  // Jubilaciones, pensiones o haberes de retiro en parcialidades
	]

	static PERCEPCIONES_NO_ACUMULABLES = [
		'001', // Reintegro de ISR pagado en exceso (siempre que no haya sido enterado al SAT).
		'002', // Subsidio para el empleo (efectivamente entregado al trabajador).
		'003', // Viáticos (entregados al trabajador).
		'004', // Aplicación de saldo a favor por compensación anual.
		'999', // Pagos distintos a los listados y que no deben considerarse como ingreso por sueldos, salarios o ingresos asimilados.
	]

}