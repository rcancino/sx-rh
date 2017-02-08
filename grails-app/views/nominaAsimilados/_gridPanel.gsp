
<table id="nominaGrid" class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<th>Folio</th>
			<th>Tipo</th>
			<th>F. Pago</th>
			<th>Periodo</th>
			<th>Pago</th>
			<th>Estadus</th>
			<th>Modificado</th>
		</tr>
	</thead>
	<tbody>
		<g:each in="${nominaInstanceList}" var="row">
			<tr>
				<td>
					<g:link action="show" id="${row.id}">
						<g:formatNumber number="${row.folio}" format="######"/>
					</g:link>
				</td>
				<td>
					<g:link action="show" id="${row.id}">
						${fieldValue(bean:row,field:"tipo")}
					</g:link>
				</td>
				<td>${row.formaDePago}</td>
				<td>${fieldValue(bean:row,field:"periodo")}</td>
				<td><g:formatDate date="${row.pago}" format="MMM dd"/></td>
				<td>${fieldValue(bean:row,field:"status")}</td>
				<td><g:formatDate date="${row.lastUpdated}" format="dd/MM/yyyy hh:mm"/></td>
			</tr>
		</g:each>
	</tbody>
</table>
