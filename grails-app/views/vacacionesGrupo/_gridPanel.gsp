<table id="vacacionesGrid" class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<th>id</th>
			<th>Comentario</th>
			<th>Fecha Ini</th>
			<th>Fecha Fin</th>
			<th>Calendario</th>
			<th>Modificado</th>
		</tr>
	</thead>
	<tbody>
		<g:each in="${vacacionesGrupoInstanceList}" var="row">
			<tr>
				<lx:idTableRow id="${row.id}"/>
				<td><g:link action="show" id="${row.id}">
						${fieldValue(bean:row,field:"comentario")}
					</g:link>
				</td>
				<td><lx:shortDate date="${row.fechaInicial}"/></td>
				<td>${row.fechaFinal.text()}</td>
				<td>${fieldValue(bean:row,field:"calendarioDet")}</td>
				<td><g:formatDate date="${row.lastUpdated}" format="dd/MM/yyyy"/></td>
			</tr>
		</g:each>
	</tbody>
</table>