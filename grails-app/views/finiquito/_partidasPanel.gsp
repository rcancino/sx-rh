<table  class="grid table table-striped table-bordered table-condensed  ">
	<thead>
		<tr>
						<th>Concepto</th>
						<th>Importe G</th>
						<th>Importe E</th>
			 
					</tr>
				</thead>
				<tbody>
					<g:each in="${entity.partidas}" var="row">
					<tr>
						<td>${row.concepto}</td>
						<td>${row.importeGravado}</td>
						<td>${row.importeExcento}</td>
					</tr>
					</g:each>
				</tbody>
</table>