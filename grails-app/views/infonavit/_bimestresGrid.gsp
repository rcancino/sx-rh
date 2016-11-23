

<table id="bimestresGrid" class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<th>Folio</th>
			<th>Ejercicio</th>
			<th>Bimestre</th>
			<th>Saldo anterior</th>
			<th>Importe bimestral</th>
			<th>Días</th>
			<th>Cuota diaria</th>
			<th>Acumulado</th>
		</tr>
	</thead>
	<tbody>
		<g:each in="${infonavitInstance.partidas.sort{!it.id}}" var="row">
			<tr>
				<td><g:formatNumber number="${row.id}" format="####"/></td>
				<td><g:formatNumber number="${row.ejercicio}" format="####"/></td>
				<td>${fieldValue(bean:row,field:"bimestre")}</td>
				<td><g:formatNumber number="${row.saldo}" format="####"/></td>
				<td><g:formatNumber number="${row.importeBimestral}" type="currency"/></td>
				<td><g:formatNumber number="${row.diasDelBimestre}" format="####"/></td>
				<td><g:formatNumber number="${row.cuotaDiaria}" type="currency"/></td>
				<td><g:formatNumber number="${row.acumulado}" format="####.####"/></td>
			</tr>
		</g:each>
	</tbody>
</table>

