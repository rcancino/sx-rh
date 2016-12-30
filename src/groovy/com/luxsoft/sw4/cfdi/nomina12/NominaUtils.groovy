package com.luxsoft.sw4.cfdi.nomina12

import java.text.SimpleDateFormat
import org.apache.xmlbeans.XmlDate

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

}