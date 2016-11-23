<%@page defaultCodec="none" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="operaciones2"/>
	<title>SDI</title>
	<r:require modules="datatables"/> 
</head>
<body>
	<content tag="header">
		<h3>Salario diario integrado Bimestre: ${session.bimestre} (${session.ejercicio })</h3>
	</content>
	
	<content tag="buttonsBar">
		
			<a href="#cambioDeBimestreDialog" class="btn btn-success btn-outline" data-toggle="modal">
				<span class="glyphicon glyphicon-search"></span> Cambiar bimestre
			</a>
			
			<g:link action="calcularSalarioDiarioIntegrado" class="btn btn-success btn-outline" 
				onclick="return confirm('Generar calculo bimestral de salario diario integrado');"
				params="[bimestre:bimestre,ejercicio:ejercicio]">
				 Calcular
			</g:link>
			<g:link action="aplicarSalarioDiarioIntegrado" 
				onclick="return confirm('Seguro que desa aplicar el SDI a todos los empleados bimestre:${session.bimestre}? ejercicio:${session.ejercicio }');" 
				class="btn btn-success btn-outline">
				 Aplicar
			</g:link>
			
	</content>
	
	<content tag="reportes">		
		<li>
			 
			<g:link action="reporte" params="[tipo:'CALCULO']"> Sdi Bimestral Calculo</g:link>
			<g:link action="reporte" params="[tipo:'VARIABLES']">Sdi Bimestral Variables</g:link>
			
		</li>
	</content>


	<content tag="gridPanel" >
		<g:render template="gridPanel"/>
		<g:render template="cambioDeBimestreDialog"/>
	</content>
	


</body>
</html>