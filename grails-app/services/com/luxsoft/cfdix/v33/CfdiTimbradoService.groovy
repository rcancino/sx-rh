package com.luxsoft.cfdix.v33

import grails.util.Environment
import grails.transaction.Transactional
import org.apache.commons.lang.exception.ExceptionUtils

import lx.cfdi.v33.Comprobante
import lx.cfdi.v33.CfdiUtils

import com.luxsoft.sw4.Empresa
import com.luxsoft.sw4.cfdi.Cfdi
import com.luxsoft.sw4.cfdi.CfdiException
import com.luxsoft.cfdix.CfdiTimbre

import com.edicom.ediwinws.cfdi.client.CfdiClient
import com.edicom.ediwinws.cfdi.utils.ZipUtils


@Transactional
public class CfdiTimbradoService {

    ZipUtils zipUtils=new ZipUtils()

    CfdiClient cfdiClient=new CfdiClient()

    Empresa empresaTransient

    def timbradoDePrueba = true

    Cfdi timbrar(Cfdi cfdi){
        try {
            String user = "PAP830101CR3"
            String password = "yqjvqfofb"
            String nombre=cfdi.xmlName
            byte[] xml=cfdi.xml
            assert xml,'El cfdi esta mal generado no contiene datos xml'
            assert nombre,"El cfdi esta mal generado no define nombre de archivo xmlName"
            byte[] zipFile=zipUtils.comprimeArchivo(nombre, xml)
            byte[] res
            if(Environment.current == Environment.PRODUCTION){
                log.debug("Timbrado de REAL Cfdi: ${cfdi.id}")
                res=cfdiClient.getCfdi(user, password, zipFile)
            }else {
                log.debug("Timbrado de prueba Cfdi: ${cfdi.id}")
                res=cfdiClient.getCfdiTest(user, password, zipFile)
            }
            Map<String, byte[]> map =zipUtils.descomprimeArchivo(res)
            Map.Entry<String, byte[]> entry=map.entrySet().iterator().next()
            
            cfdi.xmlName=entry.getKey()
            cfdi.xml=entry.getValue()
            CfdiTimbre timbre = new CfdiTimbre(cfdi)
            cfdi.uuid = timbre.uuid
            cfdi.timbrado = Date.parse("yyyy-MM-dd'T'HH:mm:ss", timbre.fechaTimbrado)
            cfdi.save(failOnError:true)
            return cfdi
        } catch (Exception e) {
            String msg="Imposible timbrar cfdi $cfdi.id Error: " + ExceptionUtils.getMessage(e)
            throw new CfdiException(message:msg,cfdi:cfdi)
        }
        return cfdi
    }


    Empresa getEmpresa() {
        if(!empresaTransient) {
            empresaTransient = Empresa.first()
        }
        return empresaTransient
    }
    

}