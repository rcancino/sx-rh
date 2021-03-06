<%@ page import="com.luxsoft.sw4.rh.NominaPorEmpleado" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Nomina de empleado</title>
</head>
<body>

	<lx:header>
		<h3>Nómina de : ${nominaPorEmpleadoInstance?.empleado}
	    		<small>${nominaPorEmpleadoInstance?.ubicacion}  (${nominaPorEmpleadoInstance?.nomina?.periodo})  Días:${nominaPorEmpleadoInstance?.nomina?.diasPagados} ${nominaPorEmpleadoInstance.nomina.tipo}</small>
	    </h3>
		<g:if test="${nominaPorEmpleadoInstance.cfdi}">
			<p><small>UUID: ${nominaPorEmpleadoInstance.cfdi.uuid}  
			 Timbrado: ${nominaPorEmpleadoInstance.cfdi.timbrado}  </small>
		</g:if>
	    
	    <lx:warningLabel/>
	    <g:if test="${ajuste}">
	        <g:render template="ajusteMensual"/>
	    </g:if> 
	</lx:header>
	
	
	<div class=" row wrapper wrapper-content  white-bg animated fadeInRight">

		<div class="col-md-3"> 
			
			<div class="panel panel-default panel-primary">
				<div class="panel-heading">Operaciones</div>
				
				<div class="list-group">
					
					<g:link  action="show" id="${nominaPorEmpleadoInstance.nomina.id}"
						class="list-group-item" >
						<span class="glyphicon glyphicon-list-alt"></span> Regresar a nómina:${nominaPorEmpleadoInstance.nomina.folio}
					</g:link>

					<g:if test="${!nominaPorEmpleadoInstance.cfdi}">
					
						<g:link class="list-group-item"
							action="actualizarNominaPorAsimilado"
							id="${nominaPorEmpleadoInstance.id}">
							<span class="glyphicon glyphicon-refresh"></span> Re-Calcular
						</g:link>
					
						<g:link class="list-group-item" action="eliminarNominaPorAsimilado" onClick="return confirm('Eliminar registro de nómina?');"
							id="${nominaPorEmpleadoInstance.id }" >
							<span class="glyphicon glyphicon-remove-circle"></span> Eliminar 
						</g:link>
					</g:if>	 
				</div>

			</div>

			<div class="panel-group " id="accordion">
			  <div class="panel panel-default panel-default">
			    <div class="panel-heading">
			      <h4 class="panel-title">
			        <a data-toggle="collapse" data-parent="#accordion" href="#collapseOne">
			          Recibo CFDI
			        </a>
			      </h4>
			    </div>

			    <div id="collapseOne" class="panel-collapse collapse ">
				    <div class="list-group">

				    	<g:if test="${!nominaPorEmpleadoInstance.cfdi}">
							
							<g:link class="list-group-item" action="generarCfdi" id="${nominaPorEmpleadoInstance.id}" >
								<i class="fa fa-file-excel-o"></i> Generar CFDI
							</g:link>
							
						</g:if>

						<g:if test="${nominaPorEmpleadoInstance.cfdi && !nominaPorEmpleadoInstance.cfdi.uuid}">
							<g:link class="list-group-item" action="timbrar" id="${nominaPorEmpleadoInstance.id}" >
								<span class="glyphicon glyphicon-screenshot"></span> Timbrar
							</g:link>
						</g:if>
						
						<g:link class="list-group-item" 
							controller="reciboDeNomina" 
							action="imprimirRecibo" id="${nominaPorEmpleadoInstance.id}">
							<span class="glyphicon glyphicon-print"></span> Imprimir recibo
						</g:link>							
						
						<g:if test="${nominaPorEmpleadoInstance.cfdi}">
												
						    <g:link class="list-group-item" action="mostrarXml"  id="${nominaPorEmpleadoInstance.cfdi.id}">
							   <span class="glyphicon glyphicon-eye-open"></span> Mostrar XML
						    </g:link>

						    <g:link class="list-group-item" action="descargarXml"  id="${nominaPorEmpleadoInstance.cfdi.id}">
							    <span class="glyphicon glyphicon-download"></span> Descargar XML
						    </g:link>
						
							
						</g:if>

						<g:if test="${nominaPorEmpleadoInstance.cfdi && nominaPorEmpleadoInstance.cfdi.uuid}">
							<g:jasperReport
									controller="reciboDeNomina"
								action="impresionDirecta"
								jasper="NominaDigitalCFDI" 
								format="PDF" 
								name="${nominaPorEmpleadoInstance.cfdi.folio }">
							<g:hiddenField name="id" value="${nominaPorEmpleadoInstance.cfdi.id}"/>
							
							</g:jasperReport>
							<g:link class="list-group-item" action="cancelar" onclick="return confirm('Cancelar CFDI?');">
							<span class="glyphicon glyphicon-ban-circle"></span> Cancelar
						    </g:link>
							
							
						
							<a: href="#" class="list-group-item">
								<span class="glyphicon glyphicon-envelope"></span> Enviar mail
							</a:>
						</g:if>
						
						</div>
			    </div>
			  </div>
			  
			  
			</div>

			
		</div>

		<div class="col-md-9">
			<div class="panel panel-default">
				<div class="panel-heading">Asistencia ${ nominaPorEmpleadoInstance?.asistencia?.periodo}</div>
				
				<table class="table">
					<tbody>
						<tr>
							<td>Salario Diario</td>
							<td>
								<lx:moneyFormat number="${nominaPorEmpleadoInstance?.salarioDiarioBase}"/>
							</td>
							<td>Percepciones</td>
							<td>
								<lx:moneyFormat number="${nominaPorEmpleadoInstance?.percepciones}"/>
							</td>
						</tr>
						<tr>
							<td>SDI</td>
							<td><lx:moneyFormat number="${nominaPorEmpleadoInstance?.salarioDiarioIntegrado}"/></td>
							<td>Deducciones</td>
							<td><lx:moneyFormat number="${nominaPorEmpleadoInstance?.deducciones}"/></td>
						</tr>
						<tr>
							<td>Subsidio</td>
							<td><lx:moneyFormat number="${nominaPorEmpleadoInstance?.subsidioEmpleoAplicado}"/></td>
							<td>Total</td>
							<td><lx:moneyFormat number="${nominaPorEmpleadoInstance?.total}"/></td>
							
						</tr>
						<tr>
							
						</tr>
						<tr>
							<td>Antigüedad</td>
							<td><g:formatNumber number="${nominaPorEmpleadoInstance?.antiguedadEnSemanas}" format="###" /></td>
							<td>Finiquito</td>
							<td>${nominaPorEmpleadoInstance.finiquito}</td>
						</tr>
						
					</tbody>
				</table>
			</div>
			
			<div class="row">
				<div class="col-md-6">
					<div class="panel panel-default">
						<div class="panel-heading">Precepciones</div>
						<g:render template="conceptos" model="[param:'PERCEPCION']"/>
					</div>

				</div>
				<div class="col-md-6">
					<div class="panel panel-default">
						<div class="panel-heading">Deducciones
							
						</div>
						<g:render template="conceptos" model="[param:'DEDUCCION']"/>
					</div>
				</div>
			</div>
		</div>

	</div>

	
	
	%{-- <g:render template="cambiarNominaDialog"/> --}%
	
	
	

</body>
</html>
