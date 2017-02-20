
<%@ page import="com.luxsoft.sw4.rh.Nomina" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Nóminas Asalariados</title>
</head>
<body>
	
	<div class="header">
		<h2> Lista de nóminas <small>(Asalariados)</small></h2>
		<g:if test="${flash.message}">
	         <lx:warningLabel></lx:warningLabel>
	    </g:if> 
	</div>

	<div class=" row wrapper wrapper-content  white-bg animated fadeInRight">
	
		<div class="col-md-12">
			<div class="button-panel">
				<div class="btn-group">
					<input type='text' id="searchField" placeholder="Filtrar" class="form-control" autocomplete="off" autofocus="autofocus">
				</div>
				
				<div class="btn-group">
					<g:link action="index" class="btn btn-default btn-outline">
						<span class="glyphicon glyphicon-repeat"></span> Refrescar
					</g:link>
						
					<button class="btn btn-primary btn-outline" data-toggle="modal" 
						data-target="#agregarNominaForm">
						<span class="glyphicon glyphicon-calendar"></span> Agregar
					</button>
					
					<div class="btn-group">
						<button type="button" name="reportes"
							class="btn btn-success btn-outline dropdown-toggle" data-toggle="dropdown"
							role="menu">
							Reportes <span class="caret"></span>
						</button>
						<ul class="dropdown-menu">
							
						</ul>
					</div>
						
				</div>
			</div>
			<g:render template="gridPanel"/>
		</div>
	</div>
		
	<g:render template="agregarDialog"/>
	
	%{--
	
	<g:render template="reporteDeNomina"/>
	<g:render template="reporteDeNominaConcentrada"/>  --}%
	

	<script type="text/javascript">
		$(function(){
			$('#nominaGrid').dataTable({
                responsive: true,
                aLengthMenu: [[20, 40, 60, 100, -1], [20, 40,60, 100, "Todos"]],
                "language": {
                    "url": "${assetPath(src: 'datatables/dataTables.spanish.txt')}"
                },
                "dom": 't<"clear">lfrip',
                "order": []
			});
			
			var table = $('#nominaGrid').DataTable();

			$("#searchField").on('keyup',function(e){
                var term=$(this).val();
                table.search(term).draw();
			});
		});
	</script>
	</div>
	
</body>
</html>
