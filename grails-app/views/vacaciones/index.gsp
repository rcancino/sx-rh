<%@ page import="com.luxsoft.sw4.rh.Vacaciones" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="operaciones"/>
	<title>Vacaciones</title>
</head>
<body>
	<content tag="header">
		<h2>Control de vacaciones ${session.ejercicio }
	        <small>(Ejercicio: ${session.ejercicio})</small>
	    </h2>
	    <ol class="breadcrumb">
		    <li><strong>Individual</strong></li>
		    <li><g:link controller="vacacionesGrupo">Grupo</g:link></li>
	    </ol>
	</content>
	
	<content tag="gridTitle">Solicitudes de vacaciones </content>
	
	<content tag="filtros">
		<div class="col-md-2">
			<input type='text' id="nombreField" placeholder="Empleado" class="form-control" autofocus="autofocus">
		</div>
		<div class="col-md-2">
			<input type='text' id="ubicacionField" placeholder="Ubicacion" class="form-control" >
		</div>
		<div class="col-md-2">
			<input type='text' id="periodicidadField" placeholder="Periodicidad" class="form-control" >
		</div>
	</content>
	
	<content tag="gridPanel">
		<g:render template="gridPanel"/>
		<script>
			$(function(){
				var table=$("#vacacionesGrid").dataTable({
			        "paging":   false,
			        "ordering": false,
			        "info":     false,
			         "dom":'t'
    				});
    				
    				$("#ubicacionField").keyup(function(){
      					table.DataTable().column(2).search( $(this).val() ).draw();
					});
					$("#nombreField").keyup(function(){
      					table.DataTable().column(1).search( $(this).val() ).draw();
					});
					$("#periodicidadField").keyup(function(){
      					table.DataTable().column(3).search( $(this).val() ).draw();
					});
					
			});
		</script>
	</content>
	
	
	
</body>
</html>