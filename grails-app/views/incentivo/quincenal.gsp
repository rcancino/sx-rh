<%@ page import="com.luxsoft.sw4.rh.Incentivo" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Incentivo Quincenal</title>
</head>
<body>
	
	<lx:header>
	    <div class="col-lg-10">
	        <h2>Control de incentivos ${tipo}</h2>
	        <a href="" data-toggle="modal" data-target="#calendarioForm">
	        	${calendarioDet.calendario.tipo}  ${calendarioDet.folio } (${ejercicio })
	        </a>
        	<ol class="breadcrumb">
        		<li ><g:link action="semanal">Semanal</g:link></li>
        	    <li ><strong>Quincenal</strong></li>
        	    <li ><g:link action="mensual">Mensual</g:link></li>
			</ol>
	        <lx:warningLabel/>
	    </div>
	</lx:header>

	<div class="row wrapper  white-bg annimated">

		<div class="row toolbar">
			
			<div class="col-md-4 ">
				<input id="filtro" class="form-control" type="text" placeholder="Filtro" autofocus="autofocus">
			</div>

			<div class="col-md-8">
				<div class="btn-group">
					<lx:refreshButton action="${webRequest.actionName}"/>
					<g:link action="generarIncentivoQuincena" 
							class="btn btn-default btn-outline btn-primary" 
							onclick="return confirm('Generar/Actualizar incentivos semana: '+' ${calendarioDet?.folio}');" 
							id="${calendarioDet.id}">
							<span class="glyphicon glyphicon-cog"></span> Generar
					</g:link>
				</div>
			</div><%-- end .col-md button panel --%>
		</div>

		<div class="row">
			<div class="col-md-12">
				<g:render template="gridPanel" />
			</div>
		</div>
	</div>
	<g:render template="calendarioPeriodoDialog"/>
	
	<script type="text/javascript">
	    $(function(){
	        $('#grid').dataTable({
	            responsive: true,
	            aLengthMenu: [[20, 40, 60, 100, -1], [20, 40,60, 100, "Todos"]],
	            "language": {
	                "url": "${assetPath(src: 'datatables/dataTables.spanish.txt')}"
	            },
	            "dom": 't<"clear">lrip',
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

	        var table = $('#grid').DataTable();

	        $("#nombreField").on('keyup',function(e){
	            var term=$(this).val();
	            table.column(1).search(term).draw();
	        });
	        $("#ubicacionField").on('keyup',function(e){
	            var term=$(this).val();
	            table.column(2).search(term).draw();
	        });

	    });
	</script>  
	
	

</body>
</html>