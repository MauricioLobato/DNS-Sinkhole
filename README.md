# DNS-Sinkhole
## DNS sinkhole feito em java.  
- Atua como uma lista negra de domínios a serem resolvidos.
- Bloqueia os domínios presentes na lista negra e utiliza servidores DNS da Google("8.8.8.8") para os não bloqueados.
- Utiliza sqlite para armazenar os domínios a serem bloqueados.  
- Projeto feito utilizando Maven.  

## Como usar:
- Mude o endereço DNS primário de seu sistema para o endereço de loopback("127.0.0.1"), caso esteja usando o mesmo host para utilizar e executar o programa.
- Mude o endereço DNS primário de seu sistema para o endereço IP do host a executar o programa, caso o host executando o programa não seja o mesmo tentando utilizá-lo.
## Informações adicionais:  
  - Para adicionar um domínio a ser bloqueado, crie um arquivo chamado __list.txt__ na raíz do projeto.
  - Adicione o(s) domínio(s) a serem bloqueados no arquivo, um por linha.
  ```
  www.example.com.br
  subdomain.example2.com
  example3.com
  ```
  - Para salvar os domínios presentes na lista no banco, execute o programa passando o seguinte argumento: __att__
  - Database.db será gerado automaticamente na raíz do projeto com os domínios de list.txt, caso ainda não tenha sido criado.
  - Para resetar a lista, delete database.db e execute novmente o programa com o argumento __att__ (como mencionado  acima).
  
  
