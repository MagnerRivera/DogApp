En esta aplicación se pueden visualizar las razas de los perros que están en el api de Dog Api, me basé en la documentación del api y por medio de este endpoint se realizó el consumo: https://restcountries.com/v3.1/all](https://dog.ceo/dog-api/

• Componentes de la aplicación (requerimientos técnicos):   
• La aplicación tiene un patrón de diseño MVVM.   
• Inyección de dependencias con Dagger Hilt.   
• Consumo del servicio con retrofit.   
• Base de datos local con ROOM tanto para el Login o creación de usuario, validación de conectividad de internet,    
• Vistas nativas con xml en su mayoría con "ConstraintLayout", "FrameLayout" y "NestedScrollView" para el tema de los recycler y fragments con mucho contenido.  
• Navegación entre fragments con Safeargs "Navigation Components" tengo un NavGraph para la navegación de los fragments tanto para el login como del home y demás vistas.   
• Procesamiento de imágenes con Picasso.  
• Uso de corrutinas para el consumo del servicio, inserción a la bd y funcionalidades en segundo plano, tanto para el consumo asíncrono del servicio.   
• El proyecto esta estructurado por capas, cada una con una funcionalidad única, igual que los fragments con responsabilidad única siguiendo los principios Solid

Especificaciones y flujo de la aplicación:

• Primera vista (Introducción):

![image](https://github.com/MagnerRivera/DogApp/assets/103458372/dbb18a15-6862-47a5-900b-0b9df7e615c9)


En esta primera vista es la introducción a la aplicación que especifica un poco que es lo que hace la APP

• Segunda vista (Opciones):

![image](https://github.com/MagnerRivera/DogApp/assets/103458372/f7ef6889-a1a3-4a16-ad69-ff12fee69393)


Al darle en el botón de "Comencemos" de la vista principal, nos llevará a esta siguiente vista que muestra 2 opciones, una llevara a otra vista para que el usuario se registre y la otra opción si el usuario ya tiene una cuenta se podrá logear con el email y contraseña

• Tercera vista (Registro):

![image](https://github.com/MagnerRivera/DogApp/assets/103458372/4fb20276-d5cd-437a-b1d5-9086a6406f39)


Al seleccionar el botón de "Registrar" de la vista anterior, llevará al usuario a esta siguiente vista, dónde se le solicitaran unos datos como: "Email", "Nombre" y "Contraseña", esta vista tiene unas validaciones para que los campos no estén vacíos, que el email este bien escrito y que la contraseña tenga mínimo 8 caracteres, de igual manera si el usuario cuenta con el correo, al seleccionar la parte que indica "¿Ya tienes una cuenta? ingresar" lo llevara a la vista del login para que el usuario se pueda logear.

• Cuarta vista (Login):

![image](https://github.com/MagnerRivera/DogApp/assets/103458372/187cfec7-4c65-4411-ab2f-14c5017755e7)  

![image](https://github.com/MagnerRivera/DogApp/assets/103458372/d87ce4c9-7758-4790-90f9-04058683f10d)



Al seleccionar el botón de "Login" o en su defeco al seleccionar "¿Ya tienes una cuenta? ingresar" de la vista de registro, no llevará a la vista de Login(Ingreso), en esta vista solicitará al usuario que digite su "correo" y "contraseña" previamente creados, si el usuario no cuenta con una cuenta, desde esta vista puede navegar a la vista de Registro y poder autenticarse en la aplicación, al usuario tener correctamente diligenciado el correo y contraseña puede navegar a la siguiente vista.

• Quinta vista (Home):

![image](https://github.com/MagnerRivera/DogApp/assets/103458372/ade575f7-5d30-498f-b692-9d7f55356d42)


El usuario al ingresar correctamente sus credenciales la aplicación le permitirá navegar a esta vista que contiene una opción de search (Buscar) con imagen de lupa, y abajo de estas opciones el usuario podrá ver todos las razas suministradas por el servicio, el usuario puede navegar hacia abajo para poder visualizar todos las razas hasta llegar a su límite, en esta vista de home en la parte inferior aparecen 2 imágenes, una Home(imagen de casa) y la otra Perfil (imagen de una persona), funcionalidades:

 Al seleccionar la lupa se desplegará una caja de texto donde el usuario podrá digitar el nombre de la raza y automáticamente la aplicación empezará a aplicar un filtro que mostrará las razas según el usuario digita en el campo de texto.

 Al seleccionar el botón de comparitr que aparece en la parte superior izquierda de cada imagen u item que aparecen este será el encargado de hacer la función de compartir a otras aplicaciones externas.

 Al seleccionar la imagen del perrito que indicaria la raza, esta lo llevara a ora vista con el detalle de la raza seleccionada.

 Al seleccionar la imagen de la persona, el usuario navegará a otra vista del perfil de usuario.

• Sexta vista (Deatil):

![image](https://github.com/MagnerRivera/DogApp/assets/103458372/aef4f9fa-1336-49fc-b523-f34ac8a8e850)


El usuario al seleccionar un perrito u raza en su defecto de la primera vista navegará a esta siguiente vista que muestra a más detalle la imagen del perrito y el nombre de la raza, en esta vista el usuario puede navegar a la vista anterior con el botón "exit" en la parte superior izquierda.

• Séptima vista (Perfil):

![image](https://github.com/MagnerRivera/DogApp/assets/103458372/d6ae77b7-e105-4971-b439-a1b0740278e3)


El usuario al navegar a esta vista podrá ver una única opción que es la de salir "Log Out", al seleccionar esta opción se mostrará una alerta al usuario con 2 opciones "Cancelar" y "Sí" si el usuario cancela se queda con el inicio de sesión que estaba, pero si le da en "Sí" se cierra la sesión y el usuario tendrá que volver a logearse.
