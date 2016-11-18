<!doctype html>
<html>
<head>
	<title>Empleados</title>
	<meta name="layout" content="luxor">
</head>
<body>

<content tag="header"> Selctor de empleados   </content>
<content tag="subHeader">
	<h3>Seleccione los empleados que requiere anexar al grupo</h3>
	<g:link action='edit' id="${operacion?.id}">
		 
	</g:link>
</content>

<content tag="document">

	<div class="wrapper wrapper-content animated fadeInRight">

		<div class="row">
			<div class="col-lg-12">
				<div class="ibox float-e-margins">
					
					<div class="ibox-title">
						<lx:backButton label="Grupo: ${operacion.id}" action="edit" id="${operacion.id}"/>
					    <a id="asignar" class="btn btn-outline btn-success">Aplicar</a>
					</div>
					
				    <div class="ibox-content">
						<table id="grid" class=" grid table table-striped table-bordered table-condensed">
							<thead>
								<tr>
									<th>Clave</th>
									<th>Nombre</th>
									<th>Status</th>
									<th>Alta</th>
									<th>Baja</th>
									<th>Ubicación</th>
									<th>Departamento</th>
								</tr>
							</thead>
							<tbody>
								<g:each in="${empleados}" var="row">
									<tr id="${row.id}">
										<td>${fieldValue(bean:row,field:"clave")}</td>
										<td>${fieldValue(bean:row,field:"nombres")}</td>
										<td>${fieldValue(bean:row,field:"status")}</td>
										<td><g:formatDate date="${row.alta}"/></td>
										<td><g:formatDate date="${row?.baja?.fecha}"/></td>
										<td>${fieldValue(bean:row,field:"perfil.ubicacion.clave")}</td>
										<td>${fieldValue(bean:row,field:"perfil.departamento.clave")}</td>
									</tr>
								</g:each>
							</tbody>
						</table>
				    </div>
				</div>
			</div>
		</div>
	</div>

	<script type="text/javascript">
		$(function(){
			$('#grid').dataTable({
                responsive: true,
                "language": {
					"url": "${assetPath(src: 'datatables/dataTables.spanish.txt')}"
	    		},
	    		"order": []
			});

			$(".grid tbody tr").click(function(){
				console.log('Seleccionando....');
				$(this).toggleClass("success selected");
			});

			var selectRows=function(){
				var res=[];
				var data=$(".grid .selected").each(function(){
					var tr=$(this);
					res.push(tr.attr("id"));
				});
				return res;
			};

			$("#asignar").on('click',function(){
				var res=selectRows();
				if(res==""){
					alert("Debe seleccionar al menos una factura");
					return
				}
				var ok=confirm('Agregar  ' + res.length+' empleado(s) al grupo:'+${operacion.id}+'?');
				if(!ok)
					return;
				$.post(
					"${createLink(action:'agregarPartidas')}",
					{id:${operacion.id},empleadosIds:JSON.stringify(res)})
				.done(function(data){
					console.log('Rres: ' + data.documento);
					window.location.href='${createLink(action:'edit',params:[id:operacion.id])}';
				}).fail(function(jqXHR, textStatus, errorThrown){
					alert("Error asignando facturas: "+errorThrown);
				});
			});
		});
	</script>
	
</content>
	

</body>
</html>


