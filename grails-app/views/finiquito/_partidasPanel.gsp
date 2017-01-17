<table  class="grid table table-striped table-bordered table-condensed  ">
	<thead>
		<tr>
			<th>Concepto</th>
			<th>Importe G</th>
			<th>Importe E</th>
			 <g:if test="${finiquitoInstance.nominaPorEmpleado == null}">
			 	<th>E</th>
			 </g:if>
		</tr>
	</thead>
	<tbody>
		<g:each in="${entity.partidas}" var="row">
		<tr class="${row.manual ? 'warning':''}">
			<td>${row.concepto} ${row.manual ? '(M)' : '(A)'}</td>
			<td>${row.importeGravado}</td>
			<td>${row.importeExcento}</td>
			<td>
				<g:if test="${finiquitoInstance.nominaPorEmpleado == null && row.manual}" >
			 	
			 		<g:link controller="finiquitoDet" action="delete" id="${row.id}" onclick="return confirm('Eliminar registro?');">
			 			<i class="fa fa-trash"></i>
			 		</g:link>
			 	
			 	</g:if>
			</td>
			
		</tr>
		</g:each>
	</tbody>
</table>