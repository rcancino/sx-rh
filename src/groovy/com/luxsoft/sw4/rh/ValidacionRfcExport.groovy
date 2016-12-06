package com.luxsoft.sw4.rh

import groovy.sql.Sql
import java.text.SimpleDateFormat

class ValidacionRfcExport{

 def dataSource

	def generarArchivo(){

		String query="""SELECT id,curp,clave,CONCAT(IFNULL(apellido_paterno,''),' ',apellido_materno,' ',nombres)as empleado,rfc,fecha_de_nacimiento,status
			,(SELECT b.fecha FROM baja_de_empleado b
			 where b.empleado_id=e.id) as baja
			FROM empleado e 
			order by e.status,baja,id
				"""
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy")

		def sql=new Sql(dataSource)
		def temp = File.createTempFile("temp",".txt",null)
		temp.with{
				append("""id,curp,clave,empleado,rfc,fecha_de_nacimiento,status,baja\r\n""" )
				sql.eachRow(query){ row->

						//println new Date(row.fecha_de_nacimiento.getTime)
						Date fechaNac=row.fecha_de_nacimiento
						Date bajaD=row.baja
						 def baja=bajaD
						if(bajaD)
							baja=df.format(bajaD)
						append(row.id+","+row.curp+","+row.clave+","+row.empleado+","+row.rfc+","+df.format(fechaNac)+","+row.status+","+baja+"\r\n")
			}
		}
		
		return temp
	}
}