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
	<h2> Cátalogo de empleados</h2>
    <ol class="breadcrumb">
    	<li>
    		<g:if test = "${tipo == 'SEMANAL'}">
    			<strong>Semanal</strong>
    		</g:if>
    		<g:else>
    			<g:link action="index" params="[tipo:'SEMANAL']">Semanal</g:link>
    		</g:else>
	    	
	    </li>
	    <li>
	    	<g:if test = "${tipo == 'QUINCENAL'}">
	    		<strong>Quincenal</strong>
	    	</g:if>
	    	<g:else>
	    		<g:link action="index" params="[tipo:'QUINCENAL']">Quincenal</g:link>
	    	</g:else>
	    </li>
	    <li>
	    	<g:if test = "${webRequest.actionName == 'bajas'}">
	    		<strong>Bajas</strong>
	    	</g:if>
	    	<g:else>
	    		<g:link action="bajas">Bajas</g:link>
	    	</g:else>
	    </li>
    </ol>
</content>

<content tag="gridPanel">
	<g:render template="grid"/>
</content>
<content tag="operaciones">
	<li><a  data-toggle="modal" data-target="#searchDialog"><i class="fa fa-search" ></i> Buscar</a></li>
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