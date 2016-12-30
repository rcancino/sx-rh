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

	static PERCEPCIONES_NO_ACUMULABLES = [
		'001', // Reintegro de ISR pagado en exceso (siempre que no haya sido enterado al SAT).
		'002', // Subsidio para el empleo (efectivamente entregado al trabajador).
		'003', // Viáticos (entregados al trabajador).
		'004', // Aplicación de saldo a favor por compensación anual.
		'999', // Pagos distintos a los listados y que no deben considerarse como ingreso por sueldos, salarios o ingresos asimilados.
	]

}