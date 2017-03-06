<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="Application"/>
	<title>Abono de prestamo</title>	
</head>
<body>

<lx:header>
		<h2 class="text-left">Prestamo: ${prestamoInstance.id}  ${prestamoInstance.empleado} </h2>
		<h3>Saldo: <g:formatNumber number="${prestamoInstance.saldo}"   type="currency"/></h3>
		<lx:errorsHeader bean="${prestamoInstance}"/>
		<lx:warningLabel/>
</lx:header>

<lx:ibox>
	<lx:iboxTitle title="Registro De Abono"/>
	<g:form name=" updateForm" class="form-horizontal" action="salvarAbono" method="POST">
		
		<lx:iboxContent>
			<g:hiddenField name="id" value="${prestamoInstance.id}" />
			<g:hiddenField name="version" value="${prestamoInstance.version}" />
			
			<div class="modal-body">
			<g:hiddenField name="prestamo.id" value="${prestamoInstance.id}"/>
			<f:with bean="${prestamoAbonoInstance}">
				<f:field property="fecha" widget-class="form-control"/>
				 <f:field property="importe" widget-class="form-control" input-type="text"/>
				<f:field property="comentario" widget-class="form-control"/>
			</f:with>
			</div>
		</lx:iboxContent>

		<lx:iboxFooter>
			<div class="btn-group">
				<lx:backButton    action="edit" id="${prestamoInstance.id}"/>
				<button type="submit" class="btn btn-outline btn-primary" >
					<i class="fa fa-floppy-save"></i> Salvar
				</button>
			</div>
		</lx:iboxFooter>
	
	</g:form>
</lx:ibox>

<script type="text/javascript">
    $(function(){
        $('.date').bootstrapDP({
            format: 'dd/mm/yyyy',
            keyboardNavigation: false,
            forceParse: false,
            autoclose: true,
            todayHighlight: true
        });

    });
 </script>

	
</body>
</html>