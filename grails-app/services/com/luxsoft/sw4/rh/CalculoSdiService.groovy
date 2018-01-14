package com.luxsoft.sw4.rh

import grails.transaction.Transactional
import groovy.sql.Sql
import com.luxsoft.sw4.Periodo
import com.luxsoft.sw4.rh.tablas.ZonaEconomica;
import com.luxsoft.sw4.Empresa

@Transactional
class CalculoSdiService {
	
	def dataSource

    def calcularSdi(ModificacionSalarial m) {
		
		def tipo=m.empleado.salario.periodicidad
		def stipo=tipo=='SEMANAL'?'SEMANA':'QUINCENA'
		def ejercicio=Periodo.obtenerYear(m.fecha)
		
		def val=CalendarioDet.executeQuery(
			"select min(d.bimestre) from CalendarioDet d where date(?) between d.inicio and d.fin and d.calendario.tipo=?"
			,[m.fecha,stipo])
		def bimestre=val.get(0)-1
		
		if(bimestre==0){
			bimestre=6
			ejercicio=ejercicio-1
		}
		
		
		
		def res=CalendarioDet
		.executeQuery(
			"select min(d.inicio),max(d.fin) from CalendarioDet d where d.bimestre=? and d.calendario.tipo=? and d.calendario.ejercicio=?"
		,[bimestre,stipo,ejercicio])
	
		def inicio=res.get(0)[0]
		def fin=res.get(0)[1]
		//println 'Res: '+res
		//println 'Inicio: '+inicio+ 'Fin: '+fin+ ' Tipo: '+stipo+ '  Ejercicio: '+ejercicio+' bBimestre: '+bimestre
		def zona=ZonaEconomica.findByClaveAndEjercicio('A',bimestre==6 ? ejercicio+1 : ejercicio)
		
		//log.info "Calculo SDI para $m.empleado $ejercicio  bimestre $bimestre tipo $tipo ${inicio.format('dd/MM/yyyy')} a ${fin.format('dd/MM/yyyy')}"

		def query=sqlPorEmpleado

		if(m.empleado.alta< fin){
 			
 			  query=sqlPorBimestre

			}


		query=query.replaceAll('@FECHA_INI'.toLowerCase(),inicio.format('yyyy/MM/dd'))
		.replaceAll('@FECHA_FIN'.toLowerCase(),fin.format('yyyy/MM/dd'))
		.replaceAll('@FECHA_ULT_MODIF'.toLowerCase(),m.fecha.format('yyyy/MM/dd'))
		.replaceAll('@TIPO'.toLowerCase()," s.periodicidad=\'SEMANAL\'")
		.replaceAll('@PERIODO'.toLowerCase(),tipo)
		//println query
		Sql sql=new Sql(dataSource)
		sql.eachRow(query,[m.empleado.id]){ row->
			def empleado=m.empleado
				
			//def found=CalculoSdi.findByEmpleadoAndEjercicioAndBimestreAndTipo(empleado,ejercicio,bimestre,m.tipo)
			//if(!found){
			def	found=new CalculoSdi(
					empleado:empleado,
					ejercicio:ejercicio,
					bimestre:bimestre,
					tipo:m.tipo,
					fechaIni:inicio,
					fechaFin:fin
					)//.save flush:true
			//}



			found.sdiAnterior=empleado.salario.salarioDiarioIntegrado
			found.sdbAnterior=empleado.salario.salarioDiario
			found.sdb=m.salarioNuevo
			found.years=( (m.fecha-empleado.alta)/365)
			found.dias=m.fecha-empleado.alta+1
			found.vacDias=row.VAC_DIAS
			found.vacPrima=row.VAC_PRIMA
			found.agndoDias=row.AGNDO_DIAS
			found.factor=row.FACTOR
			found.sdiF=found.sdb*found.factor
			found.diasLabBim=row.DIAS_LAB_BIM

			
			
			found.compensacion=0.0
			found.incentivo=0.0
			found.bonoPorDesemp=0.0
			found.bono=0.0
			found.bonoPorAntiguedad=0.0
			found.hrsExtrasDobles=0.0
			found.hrsExtrasTriples=0.0
			found.comisiones=0.0
			found.primaDom=0.0
			found.vacacionesP=0.0
			found.diaDescanso=0.0
		
			actualizarVariables(found)

			registrarTiempoExtraDoble(found)
			found.with{
				variable=compensacion+incentivo+bonoPorDesemp+bono+bonoPorAntiguedad+hrsExtrasDobles+hrsExtrasTriples+comisiones+primaDom+vacacionesP+diaDescanso
			}

			if(found.diasLabBim)
			found.varDia=found.variable/found.diasLabBim
			else 
			found.varDia=0

			def sdiNvo=found.sdiF+found.varDia
			found.sdiCalc=sdiNvo
			
			if(found.sdb==0.0){
				sdiNvo=found.varDia*found.factor
			}
			
			def topoSalarial=25*zona.uma
			found.topeSmg=topoSalarial
			
			if(sdiNvo>topoSalarial)
				found.sdiNvo=topoSalarial
			else{
				found.sdiNvo=sdiNvo
			}
			found.smg=zona.uma
			if(found.sdiAnterior==found.sdiNvo){
				found.sdiInf=0.0
			}else{
				found.sdiInf=found.sdiNvo
			}
			if(empleado.alta>fin){
				found.diasBim=0
			}else if(empleado.alta>inicio){
				found.diasBim=fin-empleado.alta+1
			}else{
				found.diasBim=fin-inicio+1
			}
			found.incapacidades=row.INCAPACIDADES
			found.faltas=row.FALTAS
			found.save failOnError:true
			log.info 'Calculo SDI Generado: '+found.id
			m.calculoSdi=found
			m.sdiNuevo=found.sdiNvo
			m.bimestre=found.bimestre
			m.save failOnError:true
		}

		

    }


    private actualizarVariables(CalculoSdi sdi){
    	
    	def partidas=NominaPorEmpleadoDet
    	.findAll("from NominaPorEmpleadoDet d where d.parent.empleado=? and d.concepto.id in(19,22,24,41,42,44,49,50,43,53) and d.parent.nomina.ejercicio=? and d.parent.nomina.calendarioDet.bimestre=?"
    			 ,[sdi.empleado,sdi.ejercicio,sdi.bimestre])
    		
    	sdi.compensacion=0.0
    	sdi.incentivo=0.0
    	sdi.bonoPorDesemp=0.0
    	sdi.bono=0.0
    	sdi.bonoPorAntiguedad=0.0
    	sdi.comisiones=0.0
    	sdi.primaDom=0.0
    	sdi.vacacionesP=0.0
    	sdi.diaDescanso=0.0

    	Empresa emp=Empresa.first()
    
    	partidas.each{it->

    		switch(it.concepto.id){
    		
    			case 19:
    				sdi.compensacion+=it.importeGravado+it.importeExcento
    				break
    			case 22:
    				sdi.incentivo+=it.importeGravado+it.importeExcento
    				break
    				/**Parche para utilizar en Kyo**/	
    			case (emp.rfc.equals("PAP830101CR3")?24:43):
    				sdi.bonoPorDesemp+=it.importeGravado+it.importeExcento
    				break
    			case 49:
    				sdi.bono+=it.importeGravado+it.importeExcento
    				break
    			case 50:
    				sdi.bonoPorAntiguedad+=it.importeGravado+it.importeExcento
    				break
    			case 41:
    				sdi.comisiones+=it.importeGravado+it.importeExcento
    				break
    			case 42:
    				if(!sdi.empleado.salario.primaDominicalFija){
    					sdi.primaDom+=it.importeGravado+it.importeExcento
    				}
    				break
    			case 44:
    				sdi.vacacionesP+=it.importeGravado+it.importeExcento
    				break
    			case 53:
    				sdi.diaDescanso+=it.importeGravado+it.importeExcento
    				break
    		}
    	}
    	
    }
    
    private registrarTiempoExtraDoble(CalculoSdi sdi){
    	def partidas=TiempoExtraDet
    	.executeQuery("from TiempoExtraDet d where d.tiempoExtra.empleado=? and d.tiempoExtra.asistencia.calendarioDet.bimestre=? and d.tiempoExtra.asistencia.calendarioDet.calendario.ejercicio=?"
    				 ,[sdi.empleado,sdi.bimestre,sdi.ejercicio])
    	
    	def triples=partidas.sum 0.0 ,{
    		it.tiempoExtraImss.integraTriples
    	}
    	def dobles=partidas.sum 0.0,{
    		if(it.tiempoExtraImss){
    		  it.tiempoExtraImss.integra
    		}
    	} 
    	sdi.hrsExtrasDobles=dobles
    	sdi.hrsExtrasTriples=triples
    
    }

	
	String sqlPorBimestre=
"""
select x.id,x.clave,x.alta
		,(select max(f.vac_dias) from factor_de_integracion f 	where round(-(timestampdiff(minute,'@fecha_ult_modif',x.alta)/60)/24,0)+1 between f.dias_de and f.dias_hasta ) as vac_dias	
		,(select max(f.vac_prima) from factor_de_integracion f where round(-(timestampdiff(minute,'@fecha_ult_modif',x.alta)/60)/24,0)+1 between f.dias_de and f.dias_hasta ) as vac_prima	
		,(select max(case 	when x.id in (245,244) then (15+2) 		
							when x.id in(274,273) then f.cob_dias when  @tipo then f.sem_dias	else f.qna_dias end	) 
			from factor_de_integracion f 	where f.tipo=(case when month('@fecha_fin')=12 then 2  when year(x.alta)=year('@fecha_fin') and month(x.alta)>=3 then 1 when year(x.alta)=year('@fecha_fin') then 0 else 2 end) and
			round(-(timestampdiff(minute,'@fecha_ult_modif',x.alta)/60)/24,0)+1 between f.dias_de and f.dias_hasta 	) as agndo_dias
		,(select max(case 	when x.id in (245,244) then 1+round((((f.vac_dias*f.vac_prima)+(15+2))/365),4) 
							when x.id in(274,273) then f.cob_factor when  @tipo then f.sem_factor else f.qna_factor end) 		
			from factor_de_integracion f where f.tipo=(case when month('@fecha_fin')=12 then 2  when year(x.alta)=year('@fecha_fin') and month(x.alta)>=3 then 1 when year(x.alta)=year('@fecha_fin') then 0 else 2 end) and 	
			round((-(timestampdiff(minute,'@fecha_ult_modif',x.alta)/60)/24),0)+1 between f.dias_de and f.dias_hasta ) as factor	
		,ifnull((case	when x.control_de_asistencia is false then  sum((select sum(a.faltas_manuales) from asistencia a where a.id=e.asistencia_id )) 
				when x.alta<='@fecha_ini' then sum(e.faltas) else round((-(timestampdiff(minute,'@fecha_fin',x.alta)/60)/24)+1,0)-round(sum(e.dias_trabajados),0) end),0) as faltas 
		,sum(e.incapacidades) as incapacidades
		,(case	when x.control_de_asistencia is false then  ifnull(round(sum(e.dias_trabajados),0)+round(sum(e.vacaciones),0)-sum((select sum(a.faltas_manuales) from asistencia a where a.id=e.asistencia_id )),0) 
				when x.alta<='@fecha_ini' then (round(((-(timestampdiff(minute,'@fecha_fin',(case when x.alta>'@fecha_ini' then x.alta else '@fecha_ini' end))/60)/24)+1),0)  -   sum(e.faltas)	-sum(e.incapacidades))  
				else  ifnull(round(sum(e.dias_trabajados),0)+round(sum(e.vacaciones),0)-sum((select sum(a.faltas_manuales) from asistencia a where a.id=e.asistencia_id )),0) end) as dias_lab_bim 
		from nomina n 						
		join nomina_por_empleado e on(e.nomina_id=n.id)
		join empleado x on(x.id=e.empleado_id)
		join salario s on(s.empleado_id=x.id)
		join perfil_de_empleado p on(p.empleado_id=x.id)
		left join baja_de_empleado b on(b.empleado_id=x.id)
		where x.id=? and s.periodicidad='@periodo' and date(n.periodo_fecha_inicial)>='@fecha_ini' and date(n.periodo_fecha_final)<='@fecha_fin'  and (b.id is null or ( x.alta>date(b.fecha) and x.status<>'baja') )
		group by x.id

"""

String sqlPorEmpleado="""
		select x.id,x.clave,x.alta
		,(select max(f.vac_dias) from factor_de_integracion f 	where round(-(timestampdiff(minute,'@fecha_ult_modif',x.alta)/60)/24,0)+1 between f.dias_de and f.dias_hasta ) as vac_dias	
		,(select max(f.vac_prima) from factor_de_integracion f where round(-(timestampdiff(minute,'@fecha_ult_modif',x.alta)/60)/24,0)+1 between f.dias_de and f.dias_hasta ) as vac_prima	
		,(select max(case 	when x.id in (245,244) then (15+2) 		
							when x.id in(274,273) then f.cob_dias when  @tipo then f.sem_dias	else f.qna_dias end	) 
			from factor_de_integracion f 	where f.tipo=(case when month('@fecha_ult_modif')=12 then 2  when year(x.alta)=year('@fecha_ult_modif') and month(x.alta)>=3 then 1 when year(x.alta)=year('@fecha_ult_modif') then 0 else 2 end) and
			round(-(timestampdiff(minute,'@fecha_ult_modif',x.alta)/60)/24,0)+1 between f.dias_de and f.dias_hasta 	) as agndo_dias
		,(select max(case 	when x.id in (245,244) then 1+round((((f.vac_dias*f.vac_prima)+(15+2))/365),4) 
							when x.id in(274,273) then f.cob_factor when  @tipo then f.sem_factor else f.qna_factor end) 		
			from factor_de_integracion f where f.tipo=(case when month('@fecha_ult_modif')=12 then 2  when year(x.alta)=year('@fecha_ult_modif') and month(x.alta)>=3 then 1 when year(x.alta)=year('@fecha_ult_modif') then 0 else 2 end) and 	
			round((-(timestampdiff(minute,'@fecha_ult_modif',x.alta)/60)/24),0)+1 between f.dias_de and f.dias_hasta ) as factor
		,0 as dias_lab_bim , 0 as faltas, 0 as incapacidades
		from empleado x
		join salario s on(s.empleado_id=x.id)
		join perfil_de_empleado p on(p.empleado_id=x.id)
		left join baja_de_empleado b on(b.empleado_id=x.id)
		where x.id=? 
		group by x.id
"""
}
