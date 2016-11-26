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
	    	<li><g:link controller="vacaciones">Individual</g:link></li>
		    <li><strong>Grupo</strong></li>
		    
	    </ol>
	</content>
	
	<content tag="gridTitle">Solicitudes de vacaciones </content>
	
	<content tag="filtros">
		<div class="col-md-2">
			<input type='text' id="filtro" placeholder="Filtrar" class="form-control" autofocus="autofocus">
		</div>
	</content>
	
	<content tag="gridPanel">

		<g:render template="gridPanel"/>
		<script>
			$(function(){
				$("#vacacionesGrid").dataTable({
			        "paging":   false,
			        "ordering": false,
			        "info":     false,
			         "language": {
                        "url": "${assetPath(src: 'datatables/dataTables.spanish.txt')}"
                    },
                    "dom": 't<"clear">Tlrip',
                    "tableTools": {
                        "sSwfPath": "${assetPath(src: 'plugins/dataTables/swf/copy_csv_xls_pdf.swf')}"
                    },
    				});
    			var table = $('#vacacionesGrid').DataTable();

				$("#filtro").keyup(function(){
					var term=$(this).val();
                	table.search(term).draw();
				});
					
					
			});
		</script>
	</content>
	
	
	
</body>
</html>