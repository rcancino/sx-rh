<%--
	 salario = 0.0
	 ptu = 0.0
	 bonoInicial = 0.0
	 bonoPreliminar = 0.0
	 porcentajeBono = 0.0
	 bono = 0.0
	
	

	 
		
	
	 
--%>
<table id="bonoGrid" class="grid gtable table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<th>Id</th>
			<th>Empleado</th>
			<th>Ubicacion</th>
			<th>T</th>
			
			<th>Faltas</th>
			<th>Incap</th>
			<th>Falt (%)</th>

			<th>Ret</th>
			<th>Ret (%)</th>

			%{-- <th>Prod</th>
			<th>Prod (%)</th> --}%

			
			<th>Bono part</th>
			<th>Bono (%)</th>
			<th>Bono</th>

			<th>IsrPorRetener</th>
			<th>SubTotal</th>
			<th>PensionA</th>
			<th>OtrasDed</th>
			<th>Prestamo</th>
			<th>NetoPagado</th>
	  
			<th>M</th>

		</tr>
	</thead>
	<tbody>
		<g:each in="${bonoInstanceList}" var="row">
			<tr>
				<td>${row.id}</td>
				<td>
					<g:link action="edit" id="${row.id}">
						${fieldValue(bean:row,field:"empleado.nombre")}
					</g:link>
				</td>
				<td>${fieldValue(bean:row,field:"empleado.perfil.ubicacion.clave")}</td>
				<td>${fieldValue(bean:row,field:"empleado.salario.periodicidad").substring(0,1)}</td>
				
				<td><g:formatNumber number="${row.faltas}" format="######"/></td>
				<td><g:formatNumber number="${row.incapacidades}" format="######"/></td>
				<td><g:formatNumber number="${row.faltasIncapacidadesFactor}" format="#,###.##"/></td>

				<td><g:formatNumber number="${row.retardos}" format="#,###.##"/></td>
				<td><g:formatNumber number="${row.retardoFactor}" format="#,###.##"/></td>

				%{-- <td>${fieldValue(bean: row, field: 'productividad')}</td>
				<td><g:formatNumber number="${row.productividadFactor}" format="##.##%"/></td> --}%

				
				<td><g:formatNumber number="${row.bonoPreliminar}" format="#,###.##"/></td> %{-- Bono part --}%
				<td><g:formatNumber number="${row.porcentajeBono}" /></td>
				<td><g:formatNumber number="${row.bono}" type="currency"/></td>

				<td><g:formatNumber number="${row.isrPorRetener}" format="#,###.##"/></td>
				<td><g:formatNumber number="${row.subTotal}" format="#,###.##"/></td>
				<td><g:formatNumber number="${row.pensionA}" format="#,###.##"/></td>
				<td><g:formatNumber number="${row.otrasDed}" format="#,###.##"/></td>
				<td><g:formatNumber number="${row.prestamo}" format="#,###.##"/></td>
				<td><g:formatNumber number="${row.netoPagado}" format="#,###.##"/></td>


				<td>
					<g:if test ="${row.manual}">
						<span class="glyphicon glyphicon-ok"></span>
					</g:if>
					<g:else>
						
					</g:else>
				</td>
				
			</tr>
		</g:each>
	</tbody>
</table>

