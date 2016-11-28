<html>
<head>
	<meta charset="UTF-8">
	<title>PTU (${ptuInstance.ejercicio})</title>
	<meta  name="layout" content="operaciones2"/>
	<r:require modules="forms,datatables"/>
</head>
<body>
	<content tag="header">
		Asignacion manual de calendario para pago de PTU del ejercicio ${ptuInstance.ejercicio}
	</content>
	<content tag="buttonsBar">
		<lx:refreshButton action="asignacionCalendario" id="${ptuInstance.id}"/>
	</content>
	<content tag="operaciones">
		<li>
			<g:link action="recalcularPagoDeBajas" class="" id="${ptuInstance.id}"
				onclick="return confirm('Recalcular PTU empleados con status BAJA?');">
				 Recalcular
			</g:link>
		</li>
		<li >
			<a href="#calendarioDialog" data-toggle="modal">Asignar calendario</a>
		</li>
	</content>
	<content tag="gridPanel">
		<g:render template="calendarioForm" model="['calendarios':calendarios]"/>
		<table id="" class=" grid table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th>Id</th>
					<th>Nombre</th>
					<th>Periodicida</th>
					<th>Ubicación</th>
					<th>E</th>
					<th>Asig</th>
					<th>Neto</th>
					<th>Monto PTU</th>
					<th>Calendario</th>
					<th>Asignar</th>
					<th>Nomina</th>
				</tr>
			</thead>
			<tbody>
				<g:each 
					in="${partidas.sort({ a,b -> a.empleado.perfil.ubicacion.clave <=> b.empleado.perfil.ubicacion.clave?: a.empleado.apellidoPaterno<=>b.empleado.apellidoPaterno  }) }" var="row">
					<tr >
						<td>${ptuInstance.id}</td>
						<td nowrap="nowrap" class="details-control">
							${fieldValue(bean:row,field:"empleado")}
						</td>
						<td>${fieldValue(bean:row,field:'empleado.salario.periodicidad')}</td>
						<td>${fieldValue(bean:row,field:'empleado.perfil.ubicacion.clave')}</td>
						<td>${fieldValue(bean:row,field:'empleado.status')}</td>
						<td>
							<g:if test="${!row.noAsignado}">
								S
							</g:if>
							<g:else>
								N
							</g:else>
						</td>
						<td>${formatNumber(number:row.porPagarNeto, type:'currency')}</td>
						<td>${formatNumber(number:row.montoPtu, type:'currency')}</td>
						<td>
							${fieldValue(bean:row,field:"calendarioDet.calendario.tipo")}
							${fieldValue(bean:row,field:"calendarioDet.calendario.comentario")}
							${fieldValue(bean:row,field:"calendarioDet.folio")}
						</td>
						<td>
							<g:if test="${!row.nominaPorEmpleado}">
								<input class="seleccionMultiple" 
									type="checkbox" 
									name="calendario" 
									value="item1"
									data-ptu="${row.id}"> 
							</g:if>
						</td>
						<td>
							${fieldValue(bean:row,field:"nominaPorEmpleado.nomina.id")}
							
						</td>
					</tr>
					
				</g:each>
			</tbody>
		</table>
	</content>
</body>
</html>