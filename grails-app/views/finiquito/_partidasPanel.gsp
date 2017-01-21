<table  class="grid table table-striped table-bordered table-condensed  ">
	<thead>
		<tr>
			%{-- <th>Id</th> --}%
			<th>Concepto</th>
			<th>Importe G</th>
			<th>Importe E</th>
			<th>Tpo</th>
			 <g:if test="${finiquitoInstance.neLiquidacion == null}">
			 	<th>E</th>
			 </g:if>
		</tr>
	</thead>
	<tbody>
		<g:each in="${entity.partidas}" var="row">
		<tr class="${row.manual ? 'warning':''}">
			%{-- <td>${row.id}</td> --}%
			<td>${row.concepto} ${row.manual ? '(M)' : '(A)'}</td>
			<td>${row.importeGravado}</td>
			<td>${row.importeExcento}</td>
			<td>${row.liquidacion ? 'L' : 'F'}</td>
			<td>
				<g:if test="${finiquitoInstance.neLiquidacion == null && row.manual}" >
			 	
			 		<g:link controller="finiquitoDet" action="delete" id="${row.id}" onclick="return confirm('Eliminar registro?');">
			 			<i class="fa fa-trash"></i>
			 		</g:link>
			 	
			 	</g:if>
			</td>
			
		</tr>
		</g:each>
	</tbody>
</table>