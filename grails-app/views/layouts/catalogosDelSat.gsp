<g:applyLayout name="application">
	<html>
	<head>
		<title><g:layoutTitle/></title>
		<g:layoutHead/>
	</head>
	</html>
	<body>
		<lx:header>
			<h3>Catálogos del SAT</h3>
		</lx:header>

		<div class=" row wrapper wrapper-content  white-bg animated fadeInRight">
			<div class="row">
				<div class="col-md-12">
					<nav:menu id="catalogosMenu" scope="app/catalogos/sat"  class="nav nav-tabs"/>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<g:pageProperty name="page.grid"/>
				</div>
			</div>
		</div>
		

		
	</body>
</g:applyLayout>