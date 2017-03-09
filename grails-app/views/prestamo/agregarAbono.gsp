<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="Application"/>
	<asset:javascript src="forms/forms.js"/>
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

                $('.chosen-select').chosen();

                $(".numeric").autoNumeric('init',{vMin:'0'},{vMax:'9999'});
                $(".money").autoNumeric('init',{wEmpty:'zero',mRound:'B',aSign: '$'});
                $(".tc").autoNumeric('init',{vMin:'0.0000'});
                $(".porcentaje").autoNumeric('init',{altDec: '%', vMax: '99.99'});

                $('form[name=updateForm]').submit(function(e){
                    console.log("Desablidatndo submit button....");

                    var button=$("#saveBtn");
                    button.attr('disabled','disabled')
                    .html('Procesando...');

                    $(".money,.porcentaje,.numeric,.tc",this).each(function(index,element){
                      var val=$(element).val();
                      var name=$(this).attr('name');
                      var newVal=$(this).autoNumeric('get');
                      $(this).val(newVal);
                    });
                    //e.preventDefault(); 
                    return true;
                });

            });
        </script> 

	
</body>
</html>