<html>
<head>
	<meta charset="UTF-8">
	<title>Genericas</title>
	<meta name="layout" content="operaciones"/>
</head>
<body>

<content tag = "header">
	<h2>Operaciones genéricas
        <small>(Ejercicio: ${session.ejercicio})</small>
    </h2>
    <ol class="breadcrumb">
	    <li><g:link action="individual" ><strong>Individual</strong></g:link></li>
	    <li><g:link controller="operacionGenericaGrupo" action="index">Grupo</g:link></li>
    </ol>
</content>	

<content tag = "gridPanel">
	<div class="row">
			<div class="col-md-12">
				<table id="grid" class="table table-striped table-bordered table-condensed">
					<thead>
						<tr>
							<th>Folio</th>
							<th>Empleado</th>
							<th>Ubicacion</th>
							<th>Tipo</th>
							<th>Calendario</th>
							%{-- <th>Nomina</th> --}%
							
						</tr>
					</thead>
					<tbody>
						<g:each in="${operacionGenericaInstanceList}" var="row">
							<tr >
								<td>
									<g:link action="show" id="${row.id}">
										<g:formatNumber number="${row.id}" format="######"/>
									</g:link>
								</td>
								<td>
									<g:link action="show" id="${row.id}">
										${fieldValue(bean:row,field:"empleado.nombre")}
									</g:link>
								</td>
								<td>${fieldValue(bean:row,field:"empleado.perfil.ubicacion.clave")}</td>
								<td>${fieldValue(bean:row,field:"tipo")}</td>
								<td>${fieldValue(bean:row,field:"calendarioDet")}</td>
								%{-- <td>${fieldValue(bean:row,field:"nominaPorEmpleadoDet")}</td> --}%
								
							</tr>
						</g:each>
					</tbody>
				</table>


			</div>
			
		</div>
</content>
	<div class="container">
		
		<div class="row">
			
			
			<div class="button-panel">
				<div class="btn-group col-md-4">
					<input type='text' id="nombreField" placeholder="Empleado" class="form-control" autofocus="autofocus" autocomplete="off">
				</div>
				<div class="btn-group">
					<input type='text' id="ubicacionField" placeholder="Ubicacion" class="form-control" autocomplete="off" >
				</div>
				<div class="btn-group">
					<g:link action="index" class="btn btn-default"><span class="glyphicon glyphicon-refresh"></span> Refrescar</g:link>
					<button type="button" name="reportes"
							class="btn btn-default dropdown-toggle" data-toggle="dropdown"
							role="menu">
							Reportes <span class="caret"></span>
					</button>
					<ul class="dropdown-menu">
						<li>
							<a href="#reportForm">
								<span class="glyphicon glyphicon-print"></span> General
							</a>
						</li>
					</ul>
				</div>
				<div class="btn-group">
					<button type="button" name="reportes"
							class="btn btn-default dropdown-toggle" data-toggle="dropdown"
							role="menu">
							Operaciones <span class="caret"></span>
					</button>
					<ul class="dropdown-menu">
						<li>
							<g:link action="create">
								<span class="glyphicon glyphicon-plus"></span> Nuevo
							</g:link>
						</li>
					</ul>
				</div>
			</div>
			
			
			
		</div><!-- end .row 2 Toolbar -->
		
		<!--  end .row 3 Grid -->
	</div>
	
	<r:script>
			$(function(){
				var table=$("#grid").dataTable({
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
					
			});
	</r:script>
</body>
</html>