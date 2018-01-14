package com.luxsoft.sw4.rh

import groovy.sql.Sql

class RevisionCalculoExport {
	
	String query="""SELECT E.id,E.clave,CONCAT(ifnull(E.apellido_paterno,'')," ",ifnull(E.apellido_materno,'')," ",E.nombres) AS nombre,P.numero_de_trabajador,E.alta,e.status,(SELECT b.fecha FROM baja_de_empleado b where b.empleado_id=e.id) as baja,(SELECT U.descripcion FROM ubicacion U WHERE p.UBICACION_ID=U.ID) AS ubicacion,s.periodicidad,cl.salario,cl.id,cl.ejercicio ,cl.ingreso_total,cl.proyectado,cl.total_gravado,cl.subs_emp_aplicado,cl.isr,cl.compensacionsaf,cl.impuesto_del_ejercicio,cl.impuesto_del_ejercicio-cl.subs_emp_aplicado as isrMenosSPEAplic,cl.resultado,cl.sueldo,cl.comisiones,cl.vacaciones,cl.vacaciones_pagadas,cl.prima_vacacional_exenta,cl.prima_vacacional_gravada,cl.premio_puntualidad,cl.incentivo,cl.aguinaldo_exento,cl.aguinaldo_gravable,cl.indemnizacion_exenta,cl.indemnizacion_gravada,cl.prima_de_antiguedad_exenta,cl.prima_de_antiguedad_gravada,cl.compensacion,cl.ptu_exenta,cl.ptu_gravada,cl.bono_de_productividad,cl.bono_por_desempeno,cl.bono,cl.bono_antiguedad,cl.bono_puntualidad_asist,cl.devispt,cl.prima_dominical_exenta,cl.prima_dominical_gravada,cl.gratificacion,cl.permiso_por_paternidad,cl.tiempo_extra_doble_exento,cl.tiempo_extra_doble_gravado,cl.tiempo_extra_triple_gravado,cl.dia_descanso,honorarios_al_consejo,cl.subs_emp_pagado,cl.total_exento,cl.total_gravado,cl.total,cl.ingreso_total,cl.retardos,cl.proyectado,cl.subs_emp_aplicado,cl.isr,cl.impuesto_del_ejercicio,cl.resultado,cl.devisptant,cl.compensacionsaf,cl.calculo_anual  
		FROM calculo_anual cl join empleado e on(e.id=cl.empleado_id) JOIN perfil_de_empleado P ON(E.ID=P.empleado_id) join salario s on(s.empleado_id=e.id) where ejercicio= ? group by e.id
		"""

	def dataSource
	
	def generarArchivo(ejercicio){

		def sql=new Sql(dataSource)
		
		def temp = File.createTempFile("temp",".txt",null)

		temp.with{
				append("""id,clave,nombre,numero_de_trabajador,alta,status,baja,ubicacion,periodicidad,salario,id,ejercicio,ingreso_total,proyectado,total_gravado,subs_emp_aplicado,isr,compensacionsaf,impuesto_del_ejercicio,isrMenosSPEAplic,resultado,sueldo,comisiones,vacaciones,vacaciones_pagadas,prima_vacacional_exenta,prima_vacacional_gravada,premio_puntualidad,incentivo,aguinaldo_exento,aguinaldo_gravable,indemnizacion_exenta,indemnizacion_gravada,prima_de_antiguedad_exenta,prima_de_antiguedad_gravada,compensacion,ptu_exenta,ptu_gravada,bono_de_productividad,bono_por_desempeno,bono,bono_antiguedad,bono_puntualidad_asist,devispt,prima_dominical_exenta,prima_dominical_gravada,gratificacion,permiso_por_paternidad,tiempo_extra_doble_exento,tiempo_extra_doble_gravado,tiempo_extra_triple_gravado,dia_descanso,honorarios_al_consejo,subs_emp_pagado,total_exento,total_gravado,total,ingreso_total,retardos,proyectado,subs_emp_aplicado,isr,impuesto_del_ejercicio,resultado,devisptant,compensacionsaf,calculo_anual\r\n""" )
				sql.eachRow(query,[ejercicio]){ row->
				
				append(row.id+','+row.clave+','+row.nombre+','+row.numero_de_trabajador+','+row.alta+','+row.status+','+(row.baja ? row.baja : ' ')+','+row.ubicacion+','+row.periodicidad+','+row.salario+','+row.id+','+row.ejercicio+','+row.ingreso_total+','+row.proyectado+','+row.total_gravado+','+row.subs_emp_aplicado+','+row.isr+','+row.compensacionsaf+','+row.impuesto_del_ejercicio+','+row.isrMenosSPEAplic+','+row.resultado+','+row.sueldo+','+row.comisiones+','+row.vacaciones+','+row.vacaciones_pagadas+','+row.prima_vacacional_exenta+','+row.prima_vacacional_gravada+','+row.premio_puntualidad+','+row.incentivo+','+row.aguinaldo_exento+','+row.aguinaldo_gravable+','+row.indemnizacion_exenta+','+row.indemnizacion_gravada+','+row.prima_de_antiguedad_exenta+','+row.prima_de_antiguedad_gravada+','+row.compensacion+','+row.ptu_exenta+','+row.ptu_gravada+','+row.bono_de_productividad+','+row.bono_por_desempeno+','+row.bono+','+row.bono_antiguedad+','+row.bono_puntualidad_asist+','+row.devispt+','+row.prima_dominical_exenta+','+row.prima_dominical_gravada+','+row.gratificacion+','+row.permiso_por_paternidad+','+row.tiempo_extra_doble_exento+','+row.tiempo_extra_doble_gravado+','+row.tiempo_extra_triple_gravado+','+row.dia_descanso+','+row.honorarios_al_consejo+','+row.subs_emp_pagado+','+row.total_exento+','+row.total_gravado+','+row.total+','+row.ingreso_total+','+row.retardos+','+row.proyectado+','+row.subs_emp_aplicado+','+row.isr+','+row.impuesto_del_ejercicio+','+row.resultado+','+row.devisptant+','+row.compensacionsaf+','+row.calculo_anual+
					','+"\r\n" )
			}
		}

		return temp
		
	}

}
