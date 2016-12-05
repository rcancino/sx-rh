<html>
<head>
	<meta charset="UTF-8">
	<title>Calculo de aguinaldo</title>
	<meta name="layout" content="operaciones2"/>
	<r:require modules="datatables"/> 
</head>
<body>



	<content tag="header" >
		<h2>Calculo de Aguinaldo  (${session.ejercicio}) 
		<small>Total Neto: ${g.formatNumber(number:aguinaldoInstanceList.sum{it.netoPagado},type:'currency')}</small>
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
				<span class="glyphicon glyphicon-plus"></span> Nuevo
			</g:link>
		</li>
		<li>
			<g:link action="actualizar">
				<span class="glyphicon glyphicon-cog"></span> Actualizar
			</g:link>
		</li>
		
	</content>

	<content tag="reportes">
		<li>
			<g:link action="reporte" params="[tipo:'BASE']"> Aguinaldo Base</g:link>
			<g:link action="reporte" params="[tipo:'CALCULO']"> Aguinaldo Cálculo</g:link>
			<g:link action="reporte" params="[tipo:'IMPUESTO']"> Aguinaldo Impuesto</g:link>
			<g:link action="reporte" params="[tipo:'PAGO']"> Aguinaldo Pago</g:link>
		</li>
	</content>


	<content tag="gridPanel">
				<div class="row">
			<div class="col-md-12">
				<!-- Nav tabs -->
				<ul class="nav nav-tabs" role="tablist">
  					<li role="presentation" class="active"><a href="#aguinaldo" role="tab" data-toggle="tab">Aguinaldo</a></li>
  					<li role="presentation"><a href="#calculo" role="tab" data-toggle="tab">Calculo</a></li>
  					<li role="presentation"><a href="#impuesto" role="tab" data-toggle="tab">Impuesto</a></li>
  					<li role="presentation"><a href="#pago" role="tab" data-toggle="tab">Pago</a></li>
				</ul>
				<!-- Tab panes -->
				<div class="tab-content">
  					<div role="tabpanel" class="tab-pane active" id="aguinaldo">
  						<g:render template="aguinaldoGrid"/>
  					</div>
  					<div role="tabpanel" class="tab-pane" id="calculo">
  						<g:render template="aguinaldoCalculoGrid"/>
  					</div>
  					<div role="tabpanel" class="tab-pane" id="impuesto">
  						<g:render template="aguinaldoCalculoImpuestoGrid"/>
  					</div>
  					<div role="tabpanel" class="tab-pane" id="pago">
  						<g:render template="aguinaldoPagoGrid"/>
  					</div>
  					
				</div>
			</div>
			
		</div>
		<g:render template="/_common/cambioGlobalDeEjercicioDialog"/>
	</content>





	
	
		<r:script>
			$(function(){
				var table=$("#aguinaldoGrid").dataTable({
			        "paging":   false,
			        "ordering": false,
			        "info":     false,
			         "dom":'t'
    				});

				var table2=$("#aguinaldoCalculoGrid").dataTable({
			        "paging":   false,
			        "ordering": false,
			        "info":     false,
			         "dom":'t'
    				});

				var table3=$("#aguinaldoCalculoImpuestoGrid").dataTable({
			        "paging":   false,
			        "ordering": false,
			        "info":     false,
			         "dom":'t'
    				});
				var table4=$("#aguinaldoPagoGrid").dataTable({
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