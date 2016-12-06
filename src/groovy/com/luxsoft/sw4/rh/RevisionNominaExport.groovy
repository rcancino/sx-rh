package com.luxsoft.sw4.rh




import groovy.sql.Sql





class RevisionNominaExport {
	

	String query="""
		SELECT E.id,E.clave,CONCAT(ifnull(E.apellido_paterno,'')," ",ifnull(E.apellido_materno,'')," ",E.nombres) AS nombre,P.numero_de_trabajador,e.alta,(SELECT b.fecha FROM baja_de_empleado b where b.empleado_id=e.id and e.alta<b.fecha ) as baja 
			,(SELECT U.descripcion FROM ubicacion U WHERE ne.UBICACION_ID=U.ID) AS ubicacion,N.tipo,n.forma_de_pago,n.periodicidad,n.folio,n.periodo_fecha_inicial,n.periodo_fecha_final,ne.dias_del_periodo
			,ne.id,ne.salario_diario_base,ne.salario_diario_integrado,ne.dias_trabajados,ne.vacaciones,ne.incapacidades,ne.faltas,ne.fraccion_descanso
			,max(case when c.clave='P001' THEN NED.importe_gravado ELSE 0 END)  AS SUELDO
			,max(case when c.clave='P029' THEN NED.importe_gravado ELSE 0 END)  AS COMISIONES
			,max(case when c.clave='P010' THEN NED.importe_gravado ELSE 0 END)  AS INCENTIVO
			,max(case when c.clave='P025' THEN NED.importe_gravado ELSE 0 END)  AS VACACIONES
			,max(case when c.clave='P031' THEN NED.importe_gravado ELSE 0 END)  AS VACACIONES_P
			,max(case when c.clave='P024' THEN NED.importe_excento ELSE 0 END)  AS PRIMA_VAC_E
			,max(case when c.clave='P024' THEN NED.importe_gravado ELSE 0 END)  AS PRIMA_VAC_G
			,max(case when c.clave='P026' THEN NED.importe_excento ELSE 0 END)  AS PRIMA_ANT_E
			,max(case when c.clave='P026' THEN NED.importe_gravado ELSE 0 END)  AS PRIMA_ANT_G
			,max(case when c.clave='P030' THEN NED.importe_excento ELSE 0 END)  AS PRIMA_DOM_E
			,max(case when c.clave='P030' THEN NED.importe_gravado ELSE 0 END)  AS PRIMA_DOM_G
			,max(case when c.clave='P002' THEN NED.importe_excento ELSE 0 END)  AS AGUINALDO_E
			,max(case when c.clave='P002' THEN NED.importe_gravado ELSE 0 END)  AS AGUINALDO_G
			,max(case when c.clave='P003' THEN NED.importe_excento ELSE 0 END)  AS PTU_E
			,max(case when c.clave='P003' THEN NED.importe_gravado ELSE 0 END)  AS PTU_G
			,max(case when c.clave='P028' THEN NED.importe_excento ELSE 0 END)  AS INDEMNIZACION_E
			,max(case when c.clave='P028' THEN NED.importe_gravado ELSE 0 END)  AS INDEMNIZACION_G
			,max(case when c.clave='P022' THEN NED.importe_excento ELSE 0 END)  AS HRS_EXTRAS_DOBLES_E
			,max(case when c.clave='P022' THEN NED.importe_gravado ELSE 0 END)  AS HRS_EXTRAS_DOBLES_G
			,max(case when c.clave='P023' THEN NED.importe_gravado ELSE 0 END)  AS HRS_EXTRAS_TRIPLES
			,max(case when c.clave='P007' THEN NED.importe_gravado ELSE 0 END)  AS COMPENSACION
			,max(case when c.clave='P034' THEN NED.importe_gravado ELSE 0 END)  AS BONO
			,max(case when c.clave='P035' THEN NED.importe_gravado ELSE 0 END)  AS BONO_ANT
			,max(case when c.clave='P011' THEN NED.importe_gravado ELSE 0 END)  AS BONO_PRODUCT
			,max(case when c.clave='P012' THEN NED.importe_gravado ELSE 0 END)  AS BONO__DESEMP
			,max(case when c.clave='P008' THEN NED.importe_gravado ELSE 0 END)  AS GRATIFICACION
			,max(case when c.clave='P032' THEN NED.importe_gravado ELSE 0 END)  AS PERMISO_PATERNID
			,max(case when c.clave='P020' THEN NED.importe_excento ELSE 0 END)  AS OTRAS_E
			,max(case when c.clave='P020' THEN NED.importe_gravado ELSE 0 END)  AS OTRAS_G
			,max(case when c.clave='P021' THEN NED.importe_excento ELSE 0 END)  AS SUB__EMP_PAG_E
			,max(case when c.clave='P019' THEN NED.importe_excento ELSE 0 END)  AS OTRAS_DEV_ISPT_E
			,Max(case when c.clave='P033' THEN NED.importe_excento ELSE 0 END)  AS COMPENSACION_SAF_E
			,ne.subsidio_empleo_aplicado  AS SUB_EMP_APLIC
			,max(case when c.clave='D001' THEN NED.importe_excento ELSE 0 END)  AS IMSS
			,max(case when c.clave='D002' THEN NED.importe_excento ELSE 0 END)  AS ISR
			,max(case when c.clave='D012' THEN NED.importe_excento ELSE 0 END)  AS RETARDOS
			,max(case when c.clave='D007' THEN NED.importe_excento ELSE 0 END)  AS PENSION_ALIMENT
			,max(case when c.clave='D009' THEN NED.importe_excento ELSE 0 END)  AS ANTICIPO
			,max(case when c.clave='D006' THEN NED.importe_excento ELSE 0 END)  AS INFONAVIT
			,max(case when c.clave='D014' THEN NED.importe_excento ELSE 0 END)  AS INFONACOT
			,max(case when c.clave='D004' THEN NED.importe_excento ELSE 0 END)  AS PRESTAMO
			,max(case when c.clave='D005' THEN NED.importe_excento ELSE 0 END)  AS OTRAS
			,Max(case when c.clave='D015' THEN NED.importe_excento ELSE 0 END)  AS RET_COMPENSACION_SAF
			FROM nomina_por_empleado_det ned 
			join nomina_por_empleado ne on(ne.id=ned.parent_id) 
			join nomina n on(n.id=ne.nomina_id) 
			join empleado e on(e.id=ne.empleado_id)
			JOIN perfil_de_empleado P ON(E.ID=P.empleado_id)
			join concepto_de_nomina c on(c.id=ned.concepto_id)
			where 
			n.calendario_det_id=?
			group by ne.id
			order by e.clave
		"""





	def dataSource
	
	def generarArchivo(id){

		def sql=new Sql(dataSource)
		def temp = File.createTempFile("temp",".txt",null)

		
		temp.with{
			append("""id,clave, nombre,numero_de_trabajador,e.alta, baja ,ubicacion,tipo,forma_de_pago,periodicidad,folio,periodo_fecha_inicial,periodo_fecha_final,dias_del_periodo,salario_diario_base,salario_diario_integrado,dias_trabajados,vacaciones,incapacidades,faltas,fraccion_descanso,SUELDO,COMISIONES,INCENTIVO,VACACIONES, VACACIONES_P, PRIMA_VAC_E, PRIMA_VAC_G, PRIMA_ANT_E, PRIMA_ANT_G,PRIMA_DOM_E, PRIMA_DOM_G, AGUINALDO_E, AGUINALDO_G, PTU_E, PTU_G,INDEMNIZACION_E,INDEMNIZACION_G, HRS_EXTRAS_DOBLES_E, HRS_EXTRAS_DOBLES_G, HRS_EXTRAS_TRIPLES,COMPENSACION, BONO, BONO_ANT, BONO_PRODUCT, BONO__DESEMP, GRATIFICACION,PERMISO_PATERNID,OTRAS_E,OTRAS_G, SUB__EMP_PAG_E,OTRAS_DEV_ISPT_E,COMPENSACION_SAF_E, SUB_EMP_APLIC,IMSS, ISR,RETARDOS,PENSION_ALIMENT, ANTICIPO, INFONAVIT, INFONACOT, PRESTAMO, OTRAS, RET_COMPENSACION_SAF\r\n""" )
				sql.eachRow(query,[id]){ row->
				append(row.id+","+row.clave+","+row.nombre+","+row.numero_de_trabajador+","+row.alta+","+row.baja+","+row.ubicacion+","+row.tipo+","+row.forma_de_pago+","+row.periodicidad+","+row.folio+","+row.periodo_fecha_inicial+","+row.periodo_fecha_final+","+row.dias_del_periodo+","+row.salario_diario_base+","+row.salario_diario_integrado+","+row.dias_trabajados+","+row.vacaciones+","+row.incapacidades+","+row.faltas+","+row.fraccion_descanso+","+row.SUELDO+","+row.COMISIONES+","+row.INCENTIVO+","+row.VACACIONES+","+row.VACACIONES_P+","+row.PRIMA_VAC_E+","+row. PRIMA_VAC_G+","+row.PRIMA_ANT_E+","+row.PRIMA_ANT_G+","+row.PRIMA_DOM_E+","+row. PRIMA_DOM_G+","+row.AGUINALDO_E+","+row.AGUINALDO_G+","+row.PTU_E+","+row.PTU_G+","+row.INDEMNIZACION_E+","+row.INDEMNIZACION_G+","+row.HRS_EXTRAS_DOBLES_E+","+row.HRS_EXTRAS_DOBLES_G+","+row.HRS_EXTRAS_TRIPLES+","+row.COMPENSACION+","+row.BONO+","+row.BONO_ANT+","+row.BONO_PRODUCT+","+row.BONO__DESEMP+","+row.GRATIFICACION+","+row.PERMISO_PATERNID+","+row.OTRAS_E+","+row.OTRAS_G+","+row.SUB__EMP_PAG_E+","+row.OTRAS_DEV_ISPT_E+","+row.COMPENSACION_SAF_E+","+row.SUB_EMP_APLIC+","+row.IMSS+","+row.ISR+","+row.RETARDOS+","+row.PENSION_ALIMENT+","+row.ANTICIPO+","+row.INFONAVIT+","+row.INFONACOT+","+row.PRESTAMO+","+row.OTRAS+","+row.RET_COMPENSACION_SAF+"\r\n" )
			}
			
		}
		
		return temp
		
		
	}

}
