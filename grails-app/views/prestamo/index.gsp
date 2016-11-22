<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="operaciones2"/>
	<title>Prestamos</title>
</head>
<body>
	<content tag="header">
		<h2>Prestamos a empleados</h2>
	</content>

	<content tag="reportes">
		<li>
			<g:jasperReport
						jasper="PrestamosResumen"
						format="PDF"
						name="Resumen">
				</g:jasperReport>
		</li>
		<li>
			<g:jasperReport
					jasper="HistoricoDePrestamos"
					format="PDF"
					name="Histórico">
				</g:jasperReport>
		</li>
	</content>
	
	<content tag="gridPanel">
		<ul class="nav nav-tabs" role="tablist">
		  <li class="active">
		  	<a href="#semanal" role="tab" data-toggle="tab">Semanal</a>
		  </li>
		  <li >
		  	<a href="#quincenal" role="tab" data-toggle="tab">Quincenal</a>
		  </li>
		</ul>
		<div class="tab-content">
	  		<div class="tab-pane active" id="semanal">
				<g:render template="gridPanel" model="['partidasList':mapList.SEMANAL]"/>
	  		</div>
	  		<div class="tab-pane" id="quincenal">
				<g:render template="gridPanel" model="['partidasList':mapList.QUINCENAL]"/>
	  		</div>
		</div>
		<script type="text/javascript">

			// var table1 = $('#gridSemanal').DataTable();

			// $("#nombreField").on('keyup',function(e){
			//     var term=$(this).val();
			//     console.log('Filtrando por: ' + term);
			//     //table1.column(1).search(term).draw();
			// });
			// $("#ubicacionField").on('keyup',function(e){
			//     var term=$(this).val();
			//     //table1.column(2).search(term).draw();
			// });
			
		</script>
	</content>
	
</body>
</html>