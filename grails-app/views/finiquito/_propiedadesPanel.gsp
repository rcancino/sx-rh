<table  class="grid table table-striped table-bordered table-condensed  ">
	<thead>
		<tr>
						<th>Propiedad</th>
						<th>Valor</th>
			 
					</tr>
				</thead>
				<tbody>
					<g:each in="${entity.properties}" var="row">
					<tr>
						<td>${row.key}</td>
						<td>${row.value}</td>
					</tr>
					</g:each>
				</tbody>
</table>