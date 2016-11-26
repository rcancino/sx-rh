
<%@ page import="com.luxsoft.sw4.rh.Nomina" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Nóminas</title>
</head>
<body>
	
	<div class="row wrapper border-bottom white-bg page-heading">
	    <div class="col-lg-10">
	        <h2> Lista de nóminas <small>(${periodicidad })</small></h2>
	        <ol class="breadcrumb">
        	    <li >
        	    	<g:link action="index" params="[periodicidad:'QUINCENAL'	]" >
        	    		<g:if test="${periodicidad=='QUINCENAL'}">
        	    			<strong>Quincenal</strong>
        	    		</g:if>
        	    		<g:else>
        	    			Quincenal
        	    		</g:else>
        	    	</g:link>
        	    </li>
        	    <li >
        	    	<g:link action="index" params="[periodicidad:'SEMANAL'	]" >
        	    		<g:if test="${periodicidad=='SEMANAL'}">
        	    			<strong>Semanal</strong>
        	    		</g:if>
        	    		<g:else>
        	    			Semanal
        	    		</g:else>
        	    	</g:link>
        	    </li>

	        </ol>
	        <g:if test="${flash.message}">
	            <small><span class="label label-warning ">${flash.message}</span></small>
	        </g:if> 
	    </div>
	</div>

	<div class=" row wrapper wrapper-content  white-bg animated fadeInRight">
	
		<div class="col-md-12">
			<div class="button-panel">
				<div class="btn-group">
					<input type='text' id="folioField" placeholder="folio" class="form-control" autocomplete="off" autofocus="autofocus">
				</div>
				<div class="btn-group">
					<input type='text' id="tipoField" placeholder="tipo" class="form-control" autocomplete="off" >
				</div>
				<div class="btn-group">
					<input type='text' id="formaDePagoField" placeholder="fomra de pago" class="form-control" autocomplete="off" >
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
												
						<li>
							<a  data-toggle="modal"	data-target="#reporteDeNominaForm"> Nomina	</a>
						</li>
						
						<li>
							<a data-toggle="modal"	data-target="#reporteDeNominaConcentradaForm"> Nomina Concentrada	</a>
						</li>
					</ul>
				</div>
						
						
						
						
						
						
						</div>
					</div>
			<g:render template="nominaGridPanel"/>
		</div>
	</div>
		
	<g:render template="agregarDialog"/>
	<g:render template="reporteDeNomina"/>
	<g:render template="reporteDeNominaConcentrada"/> 
	

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

			$("#folioField").on('keyup',function(e){
                var term=$(this).val();
                table.column(0).search(term).draw();
			});
			$("#tipoField").on('keyup',function(e){
                var term=$(this).val();
                table.column(1).search(term).draw();
			});
			$("#formaDePagoField").on('keyup',function(e){
                var term=$(this).val();
                table.column(1).search(term).draw();
			});

			$('.chosen-select').chosen();
			
		});
	</script>
	</div>
	
</body>
</html>
