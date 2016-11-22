<%@ page import="com.luxsoft.sw4.rh.Incentivo" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	%{-- <meta name="layout" content="operaciones"/> --}%
	<title>Incentivos</title>
	
</head>
<body>

	<div class="row wrapper border-bottom white-bg page-heading">
	    <div class="col-lg-10">
	        <h2>Control de incentivos ${tipo}</h2>
	        <a href="" data-toggle="modal" data-target="#calendarioSemanalForm">
	        	${calendarioDet.calendario.tipo}  ${calendarioDet.folio } (${ejercicio })
	        </a>
	        	<ol class="breadcrumb">
	        		<li ><strong>Semanal</strong></li>
            	    <li ><g:link action="quincenal">Quincenal</g:link></li>
            	    <li ><g:link action="mensual">Mensual</g:link></li>
            	    
				</ol>
	        <g:if test="${flash.message}">
	            <small><span class="label label-warning ">${flash.message}</span></small>
	        </g:if> 
	    </div>
	</div>

	<div class="row wrapper  white-bg annimated">
		
		<div class="row toolbar">
			
			<div class="col-md-4 ">
				<input id="searchField" class="form-control" type="text" placeholder="Empleado" autofocus="autofocus">
			</div>

			<div class="col-md-8">
				<div class="btn-group">
					<lx:refreshButton action="${webRequest.actionName}"/>
					<g:link action="generarIncentivoSemanal" 
							class="btn btn-default btn-outline btn-primary" 
							onclick="return confirm('Generar/Actualizar incentivos semana: '+' ${calendarioDet?.folio}');" 
							id="${calendarioDet.id}">
							<span class="glyphicon glyphicon-cog"></span> Generar
					</g:link>
				</div>
			</div><%-- end .col-md button panel --%>
		</div>

		<div class="row">
			<g:render template="gridPanel" />
		</div>
	</div>

	<g:render template="calendarioSemanalDialog"/>
	
	<script type="text/javascript">
		$(function() {
			$('#gridPanel').dataTable({
			    responsive: true,
			    aLengthMenu: [[20, 40, 60, 100, -1], [20, 40,60, 100, "Todos"]],
			    "language": {
			        "url": "${assetPath(src: 'datatables/dataTables.spanish.txt')}"
			    },
			    "dom": 'T<"clear">lfrtip',
			    "tableTools": {
			        "sSwfPath": "${assetPath(src: 'plugins/dataTables/swf/copy_csv_xls_pdf.swf')}"
			    },
			    "order": []
			});
			$("#filtro").on('keyup',function(e){
			    var term=$(this).val();
			    $('#grid').DataTable().search(
			        $(this).val()
			            
			    ).draw();
			});
		});
	</script>
	<content tag="panelBody">
		
		
		
		
		<r:script>
			$(function(){
				var table=$(".incentivoGrid").dataTable({
			        "paging":   false,
			        "ordering": false,
			        "info":     false,
			        "dom":'t'
    				});
    				
    				$("#searchField").keyup(function(){
      					table.DataTable().search( $(this).val() ).draw();
					});
			});
		</r:script>
	</content>

</body>
</html>