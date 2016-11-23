<%@page defaultCodec="none" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Empleados</title>
	<meta name="layout" content="operaciones2"/>
</head>
<body>

<content tag="header">
	<h2> Cátalogo de empleados <small>(Bajas del sitema)</small></h2>
    <ol class="breadcrumb">
    	<li><g:link action="index" params="[tipo:'SEMANAL']">Semanal</g:link></li>
	    <li><g:link action="index" params="[tipo:'QUINCENAL']">Quincenal</g:link></li>
	    <li><strong>Bajas</strong></li>
    </ol>
</content>

<content tag="gridPanel">
	<table class=" grid table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>Clave</th>
				<th>Nombre</th>
				<th>Status</th>
				<th>Baja</th>
				<th>Puesto</th>
				<th>Ubicación</th>
				<th>Departamento</th>
			</tr>
		</thead>
		<tbody>
			<g:each in="${empleadoInstanceList}" var="row">
				<tr>
					<td>
						<g:link action="show" id="${row.id}">
							${fieldValue(bean:row,field:"clave")}
						</g:link>
					</td>
					<td>
						<g:link action="show" id="${row.id}">
							${fieldValue(bean:row,field:"nombre")}
						</g:link>
					</td>
					<td>${fieldValue(bean:row,field:"status")}</td>
					<td><g:formatDate date="${row?.baja?.fecha}"/></td>
					<td>${fieldValue(bean:row,field:"perfil.puesto.clave")}</td>
					<td>${fieldValue(bean:row,field:"perfil.ubicacion.clave")}</td>
					<td>${fieldValue(bean:row,field:"perfil.departamento.clave")}</td>
					
				</tr>
			</g:each>
		</tbody>
	</table>

</content>
<content tag="operaciones">
	<li><a href=""><i class="fa fa-search" data-toggle="modal" data-target="#searchDialog"></i> Buscar</a></li>
	<g:render template="empleadoSearch"/>
</content>
<content tag="reportes">
	<li>
	    <a  data-toggle="modal"	data-target="#empleadosForm"> Empleados	</a>
	      
	</li>
	<li>
		<a data-toggle="modal"	data-target="#cumpleaniosForm"> Cumpleaños	</a>
	</li>
	<li>
		<g:jasperReport
				jasper="${'EmpleadosCalificaciones'}"
				format="PDF"
				name="Calificacion">
		</g:jasperReport>
	</li>
	<g:render template="empleados"/>
	<g:render template="dialogCumple"/>
</content>
	
	
	
	
	
	
	<content tag="searchForm">
		<g:form action="search"  class="form-horizontal" >
			<div class="modal-body">
				
				<div class="form-group">
    				<label for="apellidoPaterno" class="col-sm-3 control-label">Apellido P.</label>
    				<div class="col-sm-9">
      					<input type="text" class="form-control" id="apellidoPaterno" placeholder="Apellido paterno" name="apellidoPaterno">
    				</div>
  				</div>
  				
  				<div class="form-group">
    				<label for="apellidoPaterno" class="col-sm-3 control-label">Apellido M.</label>
    				<div class="col-sm-9">
      					<input type="text" class="form-control" id="apellidoMaterno" placeholder="Apellido materno" name="apellidoMaterno">
    				</div>
  				</div>
  				
			</div>
				<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
				<g:submitButton class="btn btn-primary" name="update" value="Buscar"/>
			</div>
		</g:form>
		
		
	</content>
	
	<script type="text/javascript">
		$(function(){
			$('#nominaGrid').dataTable({
                responsive: true,
                aLengthMenu: [[20, 40, 60, 100, -1], [20, 40,60, 100, "Todos"]],
                "language": {
                    "url": "${assetPath(src: 'datatables/dataTables.spanish.txt')}"
                },
                "dom": 't',
                "order": []
			});
			
			var table = $('#nominaGrid').DataTable();

			$('#searchForm').on('shown.bs.modal', function () {
		    	$('#apellidoPaterno').focus();
			});
			
		});
	</script>
	
	
	
</body>
</html>