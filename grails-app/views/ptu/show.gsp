<html >
<head>
	<meta charset="UTF-8" >
	 <meta  name="layout" content="operaciones2"/>
	<title>PTU (${ptuInstance.ejercicio})</title>
	
	
</head>
<body>
	
	

	<content tag="header">
		<h2>PTU del ejercicio ${ptuInstance.ejercicio}</h2>
		
		<div class="form-group">
		    <label class="col-sm-3 control-label">Monto PTU</label>
		    <div class="col-sm-3">
		      <p class="form-control-static">
		      	${formatNumber(number:ptuInstance.monto,type:'currency')}
		      </p>
		    </div>
		    <label class="col-sm-3 control-label">Remanente (${ptuInstance.ejercicio-1})</label>
		    <div class="col-sm-3">
		      <p class="form-control-static">
		      	${formatNumber(number:ptuInstance.remanente,type:'currency')}
		      </p>
		    </div>
		</div>
		<div class="form-group">
		    <label class="col-sm-3 control-label">Salario tope (Sindicalizado)</label>
		    <div class="col-sm-3">
		      <p class="form-control-static" title="${ptuInstance.empleadoTope.empleado}">
		      	${formatNumber(number:ptuInstance.sindicalizadoMaximo,type:'currency')}  (${ptuInstance.sindicalizadoNombre})
		      </p>

		    </div>
		    <label class="col-sm-3 control-label">Total</label>
		    <div class="col-sm-3">
		      <p class="form-control-static" >
		      	${formatNumber(number:ptuInstance.total,type:'currency')}
		      </p>
		    </div>
		</div>
		<div class="form-group">
		    <label class="col-sm-3 control-label">Monto Días</label>
		    <div class="col-sm-3">
		      <p class="form-control-static">
		      	${formatNumber(number:ptuInstance.montoDias,type:'currency')}
		      </p>
		    </div>
		    <label class="col-sm-3 control-label">Monto Salario</label>
		    <div class="col-sm-3">
		      <p class="form-control-static" >
		      	${formatNumber(number:ptuInstance.montoSalario,type:'currency')}
		      </p>
		    </div>
		</div>
		<div class="form-group">
		    <label class="col-sm-3 control-label">Días PTU</label>
		    <div class="col-sm-3">
		      <p class="form-control-static">
		      	${formatNumber(number:ptuInstance.diasPtu,format:'##')}
		      </p>
		    </div>
		     <label class="col-sm-3 control-label">Tope anual acu</label>
		    <div class="col-sm-3">
		      <p class="form-control-static">
		      	${formatNumber(number:ptuInstance.topeAnualAcumulado,type:'currency')}
		      </p>
		    </div>
		</div>
		<div class="form-group">
		    <label class="col-sm-3 control-label">Factor Días</label>
		    <div class="col-sm-3">
		      <p class="form-control-static">
		      	${formatNumber(number:ptuInstance.factorDias,format:'##.####')}
		      </p>
		    </div>
		    <label class="col-sm-3 control-label">Factor Salario</label>
		    <div class="col-sm-3">
		      <p class="form-control-static" >
		      	${formatNumber(number:ptuInstance.factorSalario,format:'##.####')}
		      </p>
		    </div>
		</div>
		<div class="form-group">
		    <label class="col-sm-3 control-label">Salario M.G</label>
		    <div class="col-sm-3">
		      <p class="form-control-static">
		      	${formatNumber(number:ptuInstance.salarioMinimoGeneral,type:'currency')}
		      </p>
		    </div>
		     <label class="col-sm-3 control-label">Excento PTU</label>
		    <div class="col-sm-3">
		      <p class="form-control-static">
		      	${formatNumber(number:ptuInstance.topeSmg,type:'currency')}
		      </p>
		    </div>
		   
		</div>
			
		          
	</content>

	<content tag="filtros">
		<div class="col-md-3">
	         <input id="nombreF" placeholder="Empleado" class="form-control" autofocus="autofocus" autocomplete="off">
	     </div>
	     <div class="col-md-3">
	         <input id="ubicacionF" placeholder="Ubicacion" class="form-control" autocomplete="off" >
	     </div>
	</content>

	<content tag="buttonsBar">
		<lx:refreshButton action="show" id="${ptuInstance.id}"/>
	</content>

	<content tag="operaciones">
		<li>
			<g:link action="recalcular" class="" id="${ptuInstance.id}"
				onclick="return confirm('Recalcular la PTU para todos los empleados?');">
				<span class="glyphicon glyphicon-refresh"></span> Re Calcular
			</g:link>

			<g:link action="recalcularPago" class="" id="${ptuInstance.id}"
				onclick="return confirm('Recalcular pago PTU para todos los empleados?');">
				 Recalcular Pago
			</g:link>
		</li>
		<li class="danger">
			<g:link action="delete" class="" id="${ptuInstance.id}"
				onclick="return confirm('Eliminar el calculo de PTU para el ejercicio?');">
				<span class="glyphicon glyphicon-trash"></span> Eliminar
			</g:link>
		</li>
		<li >
			<g:link action="asignacionCalendario" class="" id="${ptuInstance.id}">
				 Asignacion de calendario
			</g:link>
		</li>
	</content>

	<content tag="reportes">
		<li>
			<g:link controller="Reporte" action="reporteDePtu"  
					params="[reporte: 'CalculoPTU',ejercicio:"${ptuInstance.ejercicio}"]"> PTU</g:link>
			<g:link controller="Reporte" action="reporteDePtu"  
					params="[reporte: 'ISR_PTU',ejercicio:"${ptuInstance.ejercicio}"]"> ISR PTU</g:link>
			<g:link controller="Reporte" action="reporteDePtu"  
					params="[reporte: 'NetoPTU',ejercicio:"${ptuInstance.ejercicio}"]"> Neto PTU</g:link>
			<g:link controller="Reporte" action="reporteDePtu"  
					params="[reporte: 'AcumuladoPTU',ejercicio:"${ptuInstance.ejercicio}"]"> Acumulado PTU</g:link>
			

		</li>
		<li>
			<button class="btn btn-default" data-toggle="modal" data-target="#calendarioForm"> Recibo De PTU</button>
		</li>
	</content>
	<content tag="gridPanel">
		<g:render template="detGrid"/>
		<g:render template="reciboPTU" />
	</content>

	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="well well-sm">
					
					<form action="" class="form-horizontal">
						<div class="form-group">
						    <label class="col-sm-3 control-label">Monto PTU</label>
						    <div class="col-sm-3">
						      <p class="form-control-static">
						      	${formatNumber(number:ptuInstance.monto,type:'currency')}
						      </p>
						    </div>
						    <label class="col-sm-3 control-label">Remanente (${ptuInstance.ejercicio-1})</label>
						    <div class="col-sm-3">
						      <p class="form-control-static">
						      	${formatNumber(number:ptuInstance.remanente,type:'currency')}
						      </p>
						    </div>
						</div>
						<div class="form-group">
						    <label class="col-sm-3 control-label">Salario tope (Sindicalizado)</label>
						    <div class="col-sm-3">
						      <p class="form-control-static" title="${ptuInstance.empleadoTope.empleado}">
						      	${formatNumber(number:ptuInstance.sindicalizadoMaximo,type:'currency')}  (${ptuInstance.sindicalizadoNombre})
						      </p>

						    </div>
						    <label class="col-sm-3 control-label">Total</label>
						    <div class="col-sm-3">
						      <p class="form-control-static" >
						      	${formatNumber(number:ptuInstance.total,type:'currency')}
						      </p>
						    </div>
						</div>
						<div class="form-group">
						    <label class="col-sm-3 control-label">Monto Días</label>
						    <div class="col-sm-3">
						      <p class="form-control-static">
						      	${formatNumber(number:ptuInstance.montoDias,type:'currency')}
						      </p>
						    </div>
						    <label class="col-sm-3 control-label">Monto Salario</label>
						    <div class="col-sm-3">
						      <p class="form-control-static" >
						      	${formatNumber(number:ptuInstance.montoSalario,type:'currency')}
						      </p>
						    </div>
						</div>
						<div class="form-group">
						    <label class="col-sm-3 control-label">Días PTU</label>
						    <div class="col-sm-3">
						      <p class="form-control-static">
						      	${formatNumber(number:ptuInstance.diasPtu,format:'##')}
						      </p>
						    </div>
						     <label class="col-sm-3 control-label">Tope anual acu</label>
						    <div class="col-sm-3">
						      <p class="form-control-static">
						      	${formatNumber(number:ptuInstance.topeAnualAcumulado,type:'currency')}
						      </p>
						    </div>
						</div>
						<div class="form-group">
						    <label class="col-sm-3 control-label">Factor Días</label>
						    <div class="col-sm-3">
						      <p class="form-control-static">
						      	${formatNumber(number:ptuInstance.factorDias,format:'##.####')}
						      </p>
						    </div>
						    <label class="col-sm-3 control-label">Factor Salario</label>
						    <div class="col-sm-3">
						      <p class="form-control-static" >
						      	${formatNumber(number:ptuInstance.factorSalario,format:'##.####')}
						      </p>
						    </div>
						</div>
						<div class="form-group">
						    <label class="col-sm-3 control-label">Salario M.G</label>
						    <div class="col-sm-3">
						      <p class="form-control-static">
						      	${formatNumber(number:ptuInstance.salarioMinimoGeneral,type:'currency')}
						      </p>
						    </div>
						     <label class="col-sm-3 control-label">Excento PTU</label>
						    <div class="col-sm-3">
						      <p class="form-control-static">
						      	${formatNumber(number:ptuInstance.topeSmg,type:'currency')}
						      </p>
						    </div>
						   
						</div>


					</form>
					

				</div>
			</div>
		</div><!-- end .row 1 -->
		

	</div><!-- .container end -->
	
	<script>
		$(function(){


			var table=$("#gridDet").dataTable({
			        "paging":   false,
			        "ordering": false,
			        "info":     false,
			         "dom":'t'
    				});
			

			  $("#nombreF").keyup(function(){
      					table.DataTable().column(0).search( $(this).val() ).draw();
      					
					});
			
			
		});
	</script>

</body>
</html>