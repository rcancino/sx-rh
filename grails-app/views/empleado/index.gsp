<%@page defaultCodec="none" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Empleados</title>
	<meta name="layout" content="application"/>
</head>
<body>

	<div class="row wrapper border-bottom white-bg page-heading">
	    <div class="col-lg-10">
	        <h2> Cátalogo de empleados</h2>
	        <g:if test="${flash.message}">
	            <small><span class="label label-warning ">${flash.message}</span></small>
	        </g:if> 
	    </div>
	</div>
	
	<div class=" row wrapper wrapper-content animated white-bg fadeInRight">
		<div class="col-md-12">
			<div class="btn-group">
				<g:link action="index" class="btn btn-default btn-outline">
					<span class="glyphicon glyphicon-repeat"></span> Todos
				</g:link>
				<g:link action="create" class="btn btn-default btn-outline">
					<span class="glyphicon glyphicon-floppy-saved"></span> Nuevo
				</g:link>
				<button class="btn btn-default btn-outline" data-toggle="modal" data-target="#searchForm">
					<span class="glyphicon glyphicon-search"></span> Buscar
				</button>
				<g:link action="index" class="btn btn-default btn-outline ${tipo=='QUINCENAL'?'active':''}" params="[tipo:'QUINCENAL']">
					<span class="glyphicon glyphicon-filter"></span> Quincenal
				</g:link>
				<g:link action="index" class="btn btn-default btn-outline ${tipo=='SEMANAL'?'active':''}" params="[tipo:'SEMANAL']">
					<span class="glyphicon glyphicon-filter"></span> Semanal
				</g:link>
				<div class="btn-group">
					<button type="button" name="reportes" class="btn btn-default btn-outline dropdown-toggle" data-toggle="dropdown" role="menu">Reportes <span class="caret"></span></button>
					<ul class="dropdown-menu">
					<li>
					      <a  data-toggle="modal"	data-target="#empleadosForm"> Empleados	</a>
					      
					</li>
					<li>
						<a  data-toggle="modal"	data-target="#cumpleaniosForm"> Cumpleaños	</a>
					</li>
					<li>
							<g:jasperReport
	      						jasper="${'EmpleadosCalificaciones'}"
	      						format="PDF"
	      						name="Calificacion">
							</g:jasperReport>
					</li>
					</ul>
				</div>
			</div>
		</div>
		<div class="col-md-12">
			<g:render template="grid"/>
		</div>
	</div>
	
	<g:render template="empleados"/>
	<g:render template="dialogCumple"/>
	
	
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