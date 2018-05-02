<html>
<head>
	<meta charset="UTF-8">
	<title>Bonos de productividad</title>
	<meta name="layout" content="operaciones2"/>
	<r:require modules="datatables"/> 
</head>
<body>



	<content tag="header" >
		<h2>Bono especial  (${session.ejercicio}) 
		%{-- <small>Total Neto: ${g.formatNumber(number:aguinaldoInstanceList.sum{it.netoPagado},type:'currency')}</small> --}%
		</h2>
	</content>
	<content tag="buttonsBar">
			<g:link action="index" class="btn btn-success btn-outline">
					Refrescar
				<span class="glyphicon glyphicon-repeat"></span> 
			</g:link>
			<a data-target="#cambioGlobalDeEjercicioForm" class="btn btn-success btn-outline" data-toggle="modal">
				<span class="glyphicon glyphicon-calendar"></span> Cambiar Ejercicio
			</a>
	</content>

	<content tag="operaciones">
		<li>
			<g:link action="create">
				<span class="glyphicon glyphicon-plus"></span> Alta
			</g:link>
			<g:link action="actualizar">
				<span class="glyphicon glyphicon-refresh"></span> Actualizar
			</g:link>
		</li>
	</content>

	<content tag="reportes">
		<li>
			<g:link action="reporteBonoAnual"> Resumen de bono</g:link>
			<g:link action="reporte" params="[tipo:'IMPUESTO']"> Bono Impuesto</g:link>
			<g:link action="reporte" params="[tipo:'PAGO']"> Bono Pago</g:link>
		</li>

		
	</content>


	<content tag="gridPanel">
				<div class="row">
			<div class="col-md-12">
				<!-- Nav tabs -->
				<ul class="nav nav-tabs" role="tablist">
  					<li role="presentation" class="active"><a href="#bono" role="tab" data-toggle="tab">Bono</a></li>
				</ul>
				<!-- Tab panes -->
				<div class="tab-content">
  					<div role="tabpanel" class="tab-pane active" id="bono">
  						<g:render template="bonoGrid"/>
  					</div>
				</div>
			</div>
		</div>
		<g:render template="/_common/cambioGlobalDeEjercicioDialog"/>
	</content>
	
		<r:script>
			$(function(){
				var table=$("#bonoGrid").dataTable({
			        "paging":   false,
			        "ordering": false,
			        "info":     false,
			         "dom":'t'
    				});
    				
    				$("#nombreField").keyup(function(){
      					table.DataTable().column(0).search( $(this).val() ).draw();
      					table2.DataTable().column(0).search( $(this).val() ).draw();
      					table3.DataTable().column(0).search( $(this).val() ).draw();
      					table4.DataTable().column(0).search( $(this).val() ).draw();

					});
					
    				$("#ubicacionField").keyup(function(){
      					table.DataTable().column(1).search( $(this).val() ).draw();
      					table2.DataTable().column(1).search( $(this).val() ).draw();
      					table3.DataTable().column(1).search( $(this).val() ).draw();
      					table4.DataTable().column(1).search( $(this).val() ).draw();
					});
					
					$("#tipoField").keyup(function(){
      					table.DataTable().column(2).search( $(this).val() ).draw();
      					table2.DataTable().column(2).search( $(this).val() ).draw();
      					table3.DataTable().column(2).search( $(this).val() ).draw();
      					table4.DataTable().column(2).search( $(this).val() ).draw();
					});
					
			});
	</r:script>
</body>
</html>