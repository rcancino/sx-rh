<g:applyLayout name="application">
<html>
<head>
	<title><g:layoutTitle/></title>
	<g:layoutHead/>
</head>
</html>
<body>
<lx:header>
	<h2>Módulo de exportación de información </h2>
	<lx:warningLabel/>
</lx:header>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="col-md-4">
		<lx:ibox>
			<lx:iboxTitle title="Layouts"></lx:iboxTitle>
			<lx:iboxContent>
				<nav:menu class="nav nav-pills nav-stacked" scope="app/exportadores" />
				
				%{-- <nav:menu class="" path="app/exportadores" scope="app/exportadores" /> --}%
				%{-- <nav:menu id="nav" custom="true" path="app/exportadores" scope="app/exportadores">
				    <li class="">
				        <p:callTag tag="g:link"
				                   attrs="${linkArgs + [class:active ? 'active' : '']}">
				           <span>
				               <nav:title item="${item}"/>
				           </span>
				        </p:callTag>
				        <g:if test="${active && item.children}">
				        <nav:menu scope="${item.id}" custom="true" class="visible">
				             <li class="${item.data.icon ? 'i_'+item.data.icon : ''}">
				                 <p:callTag tag="g:link"
				                            attrs="${linkArgs + [class:active ? 'active' : '']}">
				                    <span>
				                        <nav:title item="${item}"/>
				                    </span>
				                 </p:callTag>
				         </nav:menu>
				         </g:if>
				    </li>
				</nav:menu> --}%
			</lx:iboxContent>
		</lx:ibox>
	</div>

	<div class="col-md-8">
		<lx:ibox>
			<lx:iboxContent>
				<div class="row">
					<g:pageProperty name="page.reportForm"/>	
				</div>
				
			</lx:iboxContent>
		</lx:ibox>
	</div>

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

                    

                });
	</script> 

</div>

		
		
	</body>
</g:applyLayout>