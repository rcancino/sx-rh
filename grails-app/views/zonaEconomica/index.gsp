
<%@ page import="com.luxsoft.sw4.rh.tablas.ZonaEconomica" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<g:set var="entityName" value="${message(code: 'zonaEconomica.label', default: 'ZonaEconomica')}" />
	<title><g:message code="default.list.label" args="[entityName]" /></title>
	<asset:stylesheet src="datatables/dataTables.css"/>
	<asset:javascript src="datatables/dataTables.js"/> 
</head>
<body>

	<div class="container">
		
		<div class="row">

			<div class="col-md-12">
				<div class="alert alert-info">
					<h3>
						<g:message code="zonaEconomica.list.label" 
							default='Catálogo de ZonaEconomica' />
					</h3>
					<g:if test="${flash.message}">
						<span class="label label-warning">${flash.message}</span>
					</g:if>
				</div>
			</div>
		</div><!-- end .row -->

		<div class="row toolbar-panel">
		    
		    <div class="col-md-4">
		    	<input type='text' id="filtro" placeholder="Filtrar" class="form-control" autofocus="on">
		      </div>

		    <div class="btn-group">
	        	<lx:refreshButton/>
	            <lx:printButton/>
	            <lx:createButton/>
	            <lx:searchButton/>
		    </div>
		    
		    
		</div>

		<div class="row">
			<div class="col-md-12">
				<table id="grid" class="table table-striped table-bordered table-condensed luxor-grid">
				<thead>
						<tr>
						
							<g:sortableColumn property="ejercicio" title="${message(code: 'zonaEconomica.ejercicio.label', default: 'Ejercicio')}" />
						
							<g:sortableColumn property="clave" title="${message(code: 'zonaEconomica.clave.label', default: 'Clave')}" />
						
							<g:sortableColumn property="salario" title="${message(code: 'zonaEconomica.salario.label', default: 'Salario')}" />
						
						</tr>
					</thead>
					<tbody>
					<g:each in="${zonaEconomicaInstanceList}" status="i" var="zonaEconomicaInstance">
						<tr>
						
							<td><g:link action="show" id="${zonaEconomicaInstance.id}">${fieldValue(bean: zonaEconomicaInstance, field: "ejercicio")}</g:link></td>
						
							<td>${fieldValue(bean: zonaEconomicaInstance, field: "clave")}</td>
						
							<td><g:formatNumber number="${zonaEconomicaInstance.salario}" format="#,###.##"/></td>
						
						</tr>
					</g:each>
					</tbody>
				</table>
				<div class="pagination">
					<g:paginate total="${zonaEconomicaInstanceCount ?: 0}" />
				</div>
			</div>
		</div> <!-- end .row 2 -->

	</div>
	
</body>
</html>
