# Ensembles de Julie en Hadoop

+ Projet Hadoop qui calcul les ensembles de Julia

# Authors
+ Stephane Ferreira
+ Marwan Ghanem

#Utilisation
+ A lire entierement avant d utiliser

+ Hadoop 2.6.0 doit etre deja installe et lancer sur votre machine

+ Placez vous a l aide de votre terminal dans le dossier ou vous voulez recuperer les
fichiers d entre / sortie

+ Lancez la commande suivante : hadoop jar <jarPath> main.Main <in> <out>
jarPath : chemin ou se situe le jar
in : dossier dans le hdfs ou va etre le fichier d entre 
et nom du fichier d entre en local
out : dossier dans hdfs ou vont etre cree les fichiers de sortie

+ Attention, les fichier d entre avec le meme nom seront ecrase.
Les fichiers de sortie ne seront pas ecrase, si il existe deja , aucun fichier ne
sera recupere.



# Screen

![alt tag](/screen.png)