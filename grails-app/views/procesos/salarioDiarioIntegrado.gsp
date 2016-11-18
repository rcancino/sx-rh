<%@page defaultCodec="none" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	
	<title>SDI</title>
	<r:require modules="datatables"/> 
</head>
<body>
	<content tag="header">
		<h3>Salario diario integrado Bimestre: ${bimestre} (${ejercicio })</h3>
	</content>
	
	<conente tag="buttonBar">
		
	</conente>
	
	<content tag="content">
		<div class="col-md-2">
			<input type='text' id="ubicacionField" placeholder="Ubicacion" class="form-control" autofocus="autofocus">
			
		</div>
		<div class="col-md-2">
			
			<input type='text' id="nombreField" placeholder="Empleado" class="form-control">
		</div>
		<div class="btn-group ">
			<a href="#cambioDeBimestreDialog" class="btn btn-default" data-toggle="modal">
				<span class="glyphicon glyphicon-search"></span> Cambiar bimestre
			</a>
			
			<g:link action="calcularSalarioDiarioIntegrado" class="btn btn-default" 
				onclick="return confirm('Generar calculo bimestral de salario diario integrado');"
				params="[bimestre:bimestre,ejercicio:ejercicio]">
				 Calcular
			</g:link>
			<g:link action="aplicarSalarioDiarioIntegrado" 
				onclick="return confirm('Seguro que desa aplicar el SDI a todos los empleados?');" class="btn btn-default" target="_blank"
				params="[bimestre:bimestre,ejercicio:ejercicio]">
				 Aplicar
			</g:link>
		</div>
		
		<g:render template="sdiGrid"/>
		<g:render template="cambioDeBimestreDialog"/>
		<r:script>
			$(function(){
				var table=$("#sdiGrid").dataTable({
			        "paging":   false,
			        "ordering": false,
			        "info":     false,
			         "dom":'t'
    				});
    				
    				$("#ubicacionField").keyup(function(){
      					table.DataTable().column(2).search( $(this).val() ).draw();
					});
					$("#nombreField").keyup(function(){
      					table.DataTable().column(1).search( $(this).val() ).draw();
					});
			});
		</r:script>
	</content>
</body>
</html>