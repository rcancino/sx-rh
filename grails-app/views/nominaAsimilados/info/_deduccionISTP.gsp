<%@page expressionCodec="none" %>

<table class="table table-striped table-bordered popup-table">
	<thead >
		<tr >
			<th class="text-center">Variable</th>
			<th class="text-center">Valor</th>
		</tr>
	</thead>
	<tbody class="text-right">
		
		<tr>
			<td>Honorarios</td>
			<td><g:formatNumber number="${honorarios}" type="currency"/></td>
		</tr>
		
		<tr>
			<td>Honorarios ISR </td>
			<td><g:formatNumber number="${impuestoPorHonorarios}" type="currency"/></td>
		</tr>
		
		<tr>
			<td>Salario Mensual</td>
			<td><g:formatNumber number="${salarioMensual}" type="currency"/></td>
		</tr>

		<tr>
			<td>Salario Mensual ISR</td>
			<td><g:formatNumber number="${impuestoMensual}" type="currency"/></td>
		</tr>

		<tr>
			<td>Ingreso Total</td>
			<td><g:formatNumber number="${ingresoTotal}" type="currency"/></td>
		</tr>

		<tr>
			<td>Ingreso Total ISR</td>
			<td><g:formatNumber number="${impuestoTotal}" type="currency"/></td>
		</tr>
		
		%{-- <tr>
			<td>Límite inferior </td>
			<td>
				<g:formatNumber number="${tarifa.limiteInferior}" type="currency"/>
				
			</td>
			
		</tr>
		<tr>
			<td>Exced s/Lim inf</td>
			<td><g:formatNumber number="${baseGravable}" type="currency"/></td>
		</tr>
		<tr>
			<td>Tasa %</td>
			<td><g:formatNumber number="${tarifaPorcentaje*100}" format="##.##"/></td>
		</tr>
		<tr>
			<td>Impuesto marginal</td>
			<td><g:formatNumber number="${importeGravado}" type="currency"/></td>
		</tr>
		<tr>
			<td>Cuota fija</td>
			<td><g:formatNumber number="${cuotaFija}" type="currency"/></td>
		</tr>
		<tr>
			<td>Impuesto </td>
			<td><g:formatNumber number="${istp}" type="currency"/></td>
		</tr>
		<tr>
			<td>Subsidio</td>
			<td>0.0</td>
		</tr> --}%
		
	</tbody>
	<tfoot>
		<tr>
			<th>ISR Ajustado</th>
			<th class="text-right"><g:formatNumber number="${impuestoAjustado}" type="currency"/></th>
		</tr>
	</tfoot>
</table>




