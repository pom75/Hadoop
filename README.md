# Ensembles de Julie en Hadoop

Projet Hadoop qui calcul les ensembles de Julia

# Authors
St�ephane Ferreira
Marwan Ghanem

#Utilisation
A lire enti�rement avant d�utiliser

Hadoop 2.6.0 doit �tre d�j� install� et lancer sur votre machine

Placez vous � l�aide de votre terminal dans le dossier o� vous voulez r�cup�rer les
fichiers d�entr� / sortie

Lancez la commande suivante : hadoop jar <jarPath> main.Main <in> <out>
jarPath : chemin ou se situe le jar
in : dossier dans le hdfs o� va �tre le fichier d�entr� 
+ nom du fichier d�entr� en local
out : dossier dans hdfs ou vont �tre cr�e les fichiers de sortie

Attention, les fichier d�entr� avec le meme nom seront �cras�.
Les fichiers de sortie ne seront pas �cras�, si ilx existe d�j� , aucun fichier ne
sera r�cup�r�.



# Screen

![alt tag](/screen.png)