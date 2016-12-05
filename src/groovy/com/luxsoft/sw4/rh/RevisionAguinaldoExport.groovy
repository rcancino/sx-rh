package com.luxsoft.sw4.rh




import groovy.sql.Sql





class RevisionAguinaldoExport {
	

	String query="""select e.id,e.clave,CONCAT(IFNULL(e.apellido_paterno,''),' ',e.apellido_materno,' ',e.nombres) as nombre,p.numero_de_trabajador,s.periodicidad,x.descripcion as puesto,u.descripcion as ubicacion,e.alta,e.status ,b.fecha as f_baja	
		,ROUND((-(TIMESTAMPDIFF(MINUTE,DATE(( CASE WHEN B.FECHA<a.fecha_final AND B.FECHA>e.ALTA THEN B.FECHA ELSE a.fecha_final END )),e.ALTA)/60)/24)/365,2)  AS years		
		,ROUND(-(TIMESTAMPDIFF(MINUTE,DATE(( CASE WHEN B.FECHA<a.fecha_final  AND B.FECHA>e.ALTA THEN B.FECHA ELSE a.fecha_final END )),e.ALTA)/60)/24,0)  AS dias	
		,a.id,a.ejercicio,a.fecha_inicial,a.fecha_final,a.salario,a.faltas,a.incapacidades,a.incapacidadesmat,a.incapacidadesrte,a.incapacidadesrtt,a.permiso_especial	
		,a.dias_para_aguinaldo,a.dias_de_aguinaldo,a.aguinaldo,a.dias_para_bono,a.dias_de_bono,a.bono_preliminar,a.porcentaje_bono,a.bono	
		,a.aguinaldo_excento,a.aguinaldo_gravado,a.total_gravable,a.promedio_gravable,a.sueldo_mensual,a.proporcion_promedio_mensual	
		,a.isr_mensual,a.isr_promedio,a.dif_isr_mensual_promedio,a.tasa,a.isr_por_retener,a.subsidio,a.isrosubsidio,a.resultado_isr_subsidio,a.tabla_normal_isr_sub,a.beneficio_perjuicio,a.isr_ejer_ant	
		,a.sub_total,a.pensiona,a.otras_ded ,a.prestamo,a.neto_pagado
		from aguinaldo a
		join empleado e on(e.id=a.empleado_id)
		join perfil_de_empleado p on(p.empleado_id=e.id)
		join salario s on(s.empleado_id=e.id)
		join ubicacion u on(u.id=p.ubicacion_id)
		join puesto x on(x.id=p.puesto_id)
		left join baja_de_empleado b on(b.empleado_id=e.id)
		where  a.ejercicio=?
		order by periodicidad,clave
		"""





	def dataSource
	
	def generarArchivo(ejercicio){

		def sql=new Sql(dataSource)

		println "Generando Archivo de Aguinaldo"+ejercicio
		def temp = File.createTempFile("temp",".txt",null)



		
		temp.with{
				append("""id,clave,nombre,numTrab,periodicidad,puesto,ubicacion,alta,status,f_baja,years,dias,ejercicio,fecha_inicial,fecha_final,salario,faltas,incEG,incapacidadesmat,incapacidadesrte,incapacidadesrtt,permiso_especial,aguinaldo_excento,aguinaldo_gravado,total_gravable,promedio_gravable,sueldo_mensual,proporcion_promedio_mensual,isr_mensual,isr_promedio,dif_isr_mensual_promedio,tasa,isr_por_retener,subsidio,isrosubsidio,resultado_isr_subsidio,tabla_normal_isr_sub,beneficio_perjuicio,isr_ejer_ant,sub_total,pensiona,otras_ded,prestamo,neto_pagado\r\n""" )
				sql.eachRow(query,[ejercicio]){ row->
				println row
				append(row.id+','+row.clave+','+row.nombre+','+row.numero_de_trabajador+','+row.periodicidad+','+row.puesto+','+row.ubicacion+','+row.alta+','+row.status+','+row.f_baja+','+row.years+','+row.dias+','+row.ejercicio+','+row.fecha_inicial+','+row.fecha_final+','+row.salario+','+row.faltas+','+row.incapacidades+','+row.incapacidadesmat+','+row.incapacidadesrte+','+row.incapacidadesrtt+','+row.permiso_especial+','+row.aguinaldo_excento+','+row.aguinaldo_gravado+','+row.total_gravable+','+row.promedio_gravable+','+row.sueldo_mensual+','+row.proporcion_promedio_mensual+','+row.isr_mensual+','+row.isr_promedio+','+row.dif_isr_mensual_promedio+','+row.tasa+','+row.isr_por_retener+','+row.subsidio+','+row.isrosubsidio+','+row.resultado_isr_subsidio+','+row.tabla_normal_isr_sub+','+row.beneficio_perjuicio+','+row.isr_ejer_ant+','+row.sub_total+','+row.pensiona+','+row.otras_ded+','+row.prestamo+','+row.neto_pagado+"\r\n" )
			}
		}
		
		return temp
		
		
	}

}
