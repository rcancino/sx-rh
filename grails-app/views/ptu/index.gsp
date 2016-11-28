<html>
<head>
	<meta charset="UTF-8">
	<title>Calculo de PTU</title>

</head>
<body>

	<div class="row wrapper border-bottom white-bg page-heading">
	    <div class="col-lg-10">
				<h2>Modulo para el calculo del PTU</h2>
	        <lx:warningLabel/>
	        <g:if test="${flash.error}">
	            <small><span class="label label-danger ">${flash.error}</span></small>
	        </g:if> 
	    </div>
	</div>
	<div class=" row wrapper wrapper-content  white-bg animated fadeInRight">
		<div class="row toolbar">
			<div class="col-md-3">
                 <input id="ejercicioField" placeholder="Ejercicio" class="form-control" autofocus="autofocus" autocomplete="off">
             </div>
			<div class="btn-group">
				<g:link action="index" class="btn btn-success btn-outline">
						Refrescar
					<span class="glyphicon glyphicon-repeat"></span> 
				</g:link>
					<a data-target="#cambioGlobalDeEjercicioForm" class="btn btn-success btn-outline" data-toggle="modal">
						<span class="glyphicon glyphicon-calendar"></span> Cambiar Ejercicio
					</a>
				<g:link action="create" class="btn btn-success btn-outline">
						<span class="glyphicon glyphicon-plus"></span> Nuevo
				</g:link>
	             <button type="button" name="reportes"
	                     class="btn btn-primary btn-outline dropdown-toggle" data-toggle="dropdown"
	                     role="menu">
	                     Reportes <span class="caret"></span>
	             </button>
	             <ul class="dropdown-menu">
	                <li>
						<g:link action="reporteGlobal" > PTU General</g:link>
						<g:link action="reporteGlobal" > PTU Individual</g:link>
					</li>
	             </ul>
	         </div>
		</div>
		<g:render template="grid"/>
		<g:render template="/_common/cambioGlobalDeEjercicioDialog"/>
	</div>

	
	<script>
		$(function(){
			var table=$("#grid").dataTable({
		        "paging":   false,
		        "ordering": false,
		        "info":     false,
		         "dom":'t'
				});
				
			var tables = $('#grid'); //.DataTable();

        		$("#ejercicioField").on('keyup',function(e){
            	var term=$(this).val();
            $.each(tables, function(p,v) {
                //console.log('Pro: ' + p);
                //console.log('Val: ' + v);
                $(this).DataTable().column(0).search(term).draw();
                
            });
        });	
				
			
				
		});
	</script>
</body>
</html>