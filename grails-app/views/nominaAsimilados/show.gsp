<%@ page import="com.luxsoft.sw4.rh.Nomina" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Nomina (${nominaInstance.id})</title>
</head>
<body>
	
	<g:set var="pendientesDeTimbrado" 
		value="${nominaInstance.partidas.count{ (it?.cfdi?.uuid == null) && it.total>0.0}}" scope="request" />
	
	<div class="row wrapper border-bottom white-bg page-heading">
	    <div class="col-lg-10">
	    	<g:link action="index" id="${nominaInstance.id}" params="[periodicidad:nominaInstance.periodicidad]">
	    		<h2>Nómina: ${nominaInstance.folio} ${nominaInstance.periodicidad}  
	    			<small> ${nominaInstance.formaDePago }  (${nominaInstance.periodo}) ${nominaInstance.tipo} (${nominaInstance.status})</small>
	    		</h2>
	    		%{-- <g:if test= "${nominaInstance.status == 'CERRADA'}">
	    			
	    		</g:if> --}%
	    	</g:link>
	    	<g:if test="${nominaInstance.status == 'CERRADA'}">
	    		<small><span class='label label-warning' >NOMINA CERRADA</small>
	    		<g:if test ="${nominaInstance.partidas.count{it?.cfdi?.uuid == null} > 0 }">
	    			Con ${pendientesDeTimbrado} registros pendientes de timbrado
	    		</g:if>
	    	</g:if>

	        <g:if test="${flash.message}">
	            <small><span class="label label-warning ">${flash.message}</span></small>
	        </g:if> 
	    </div>
	</div>
	
	<div class=" row wrapper wrapper-content  white-bg animated fadeInRight">
		
		<div class="col-md-12">
			<div class="btn-group">
				<g:link controller="nomina" action="show" id="${nominaInstance.id}" class="btn btn-default btn-outline">
					<span class="glyphicon glyphicon-repeat"></span> Refrescar
				</g:link>
			</div>
			<div class="btn-group">
				<button type="button" name="reportes" class="btn btn-default btn-outline dropdown-toggle" data-toggle="dropdown" role="menu">Reportes <span class="caret"></span></button>
				<ul class="dropdown-menu">
					<li>
						<g:jasperReport
          						jasper="NominaCaratula"
          						format="PDF"
          						name="Carátula">
    							<g:hiddenField name="NOMINA" value="${nominaInstance.id}"/>
 						</g:jasperReport>
					</li>
					
					<g:if test="${['QUINCENAL','SEMANAL'].contains(nominaInstance.periodicidad) }">
						
						
						<li>
							<g:jasperReport
          						jasper="${nominaInstance.periodicidad=='QUINCENAL'?'NominaCaratulaQ':'NominaCaratulaS' }"
          						format="PDF"
          						name="Resumen">
    							<g:hiddenField name="NOMINA" value="${nominaInstance.id}"/>
 							 </g:jasperReport>
						</li>
						
					</g:if>
					<li>
						<button class="btn btn-default btn-outline" data-toggle="modal" data-target="#detalleDeNominaForm"> Detalle</button>
					</li>
				</ul>
			</div> <!-- Fin .btn-group -->
						
			<div class="btn-group">
				<button type="button" name="reportes" class="btn btn-default btn-outline dropdown-toggle" data-toggle="dropdown" >Operaciones <span class="caret"></span></button>
				<ul class="dropdown-menu">
					
					<li>
						<g:link  action="agregarNominaPorEmpleado" id="${nominaInstance.id}"> 
							<span class="glyphicon glyphicon-plus"></span> Agregar
						</g:link> 
					</li>	
					
					<li>
						<g:link action="delete" id="${nominaInstance.id}" onclick="return confirm('Eliminar toda la nomina?');"> 
							<span class="glyphicon glyphicon-trash"></span> Eliminar
						</g:link> 
					</li>
					<li>
						<g:link action="generarCfdis" id="${nominaInstance.id}" onclick="return confirm('Generar cfdi's (XML)  toda la nomina?');"> 
							 Generar CFDI's
						</g:link> 
					</li>
					<li>
						<g:link action="timbrar" id="${nominaInstance.id}" onclick="return confirm('Timbrar toda la nomina?');"> 
							 Timbrar CFDI's
						</g:link> 
					</li>
					
					
					
				</ul>
			</div> <!-- Fin .btn-group -->
		</div>

		
		
		<div class="col-md-12">
			<div class="tab-content">
			<br>
				<g:render template="nominaDetGridPanel" />
			</div>
		</div>
	
	</div>

	%{-- <g:render template="reporteDetalleDeNominaDialog"/> --}%
	
	<content tag="grid">

		
	</content>



</body>
</html>
