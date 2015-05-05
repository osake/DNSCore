#!/bin/bash

# author: Daniel M. de Oliveira

HIER=`pwd`

if [ "$1" = "populate" ]
then
	sqls=(
		"DELETE FROM subformat_identification_strategy_puid_mappings;"
		"DELETE FROM queue;"
		"DELETE FROM events;"
		"DELETE FROM documents;"
		"DELETE FROM dafiles;"
		"DELETE FROM objects_packages;"
		"DELETE FROM copies;"
		"DELETE FROM packages;"
		"DELETE FROM objects;"
		"DELETE FROM nodes_contractors;"
		"DELETE FROM nodes;"
		"DELETE FROM conversion_policies;"
		"DELETE FROM conversion_routines;"
		"DELETE FROM conversion_queue;"
		"DELETE FROM user_role;"		
		"DELETE FROM preservation_system;"
		"DELETE FROM users;"
		"DELETE FROM Role;"
		"INSERT INTO users (id,short_name,username,password,accountlocked,accountexpired,passwordexpired,enabled,email_contact) values (1,'TEST','TEST','\$2a\$10\$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.',FALSE,FALSE,FALSE,TRUE,'noreply');"
        "INSERT INTO users (id,short_name,username,password,accountlocked,accountexpired,passwordexpired,enabled,email_contact) values (4,'rods','rods','\$2a\$10\$ZTCS5CwF2uB.URbl8hDtO.fvwTuApfZKHSOtb6dHwXZhZMnKqshhe',FALSE,FALSE,FALSE,TRUE,'noreply');"
        "INSERT INTO users (id,short_name,username,password,accountlocked,accountexpired,passwordexpired,enabled,email_contact) values (5,'CI_ADMIN','CI_ADMIN','\$2a\$10\$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.',FALSE,FALSE,FALSE,TRUE,'noreply');"
"INSERT INTO users (id,short_name,username,password,accountlocked,accountexpired,passwordexpired,enabled,email_contact) values (6,'CI_ADMIN','N2_ADMIN','\$2a\$10\$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.',FALSE,FALSE,FALSE,TRUE,'noreply');"
		"INSERT INTO role (id,authority) values (1,'ROLE_PSADMIN');"
		"INSERT INTO role (id,authority) values (2,'ROLE_CONTRACTOR');"
		"INSERT INTO role (id,authority) values (3,'ROLE_SYSTEM');"
		"INSERT INTO role (id,authority) values (4,'ROLE_NODEADMIN');"
		"INSERT INTO user_role (user_id,role_id) values (1,2);"
		"INSERT INTO user_role (user_id,role_id) values (4,1);"
		"INSERT INTO user_role (user_id,role_id) values (4,2);"
		"INSERT INTO user_role (user_id,role_id) values (4,4);"
		"INSERT INTO user_role (user_id,role_id) values (5,1);"
		"INSERT INTO user_role (user_id,role_id) values (5,4);"
		"INSERT INTO user_role (user_id,role_id) values (6,4);"
		"INSERT INTO preservation_system (id,urn_name_space,sidecar_extensions,pres_server,open_collection_name,closed_collection_name,uris_aggr,uris_cho,uris_file,min_repls,uris_local,admin_id) VALUES (1,'urn:nbn:de:danrw','xmp;txt;xml','localnode','collection-open','collection-closed','http://data.danrw.de/aggregation','http://data.danrw.de/cho','http://data.danrw.de/file',1,'info:',5);"
		"INSERT INTO nodes (id,urn_index,name,identifier,admin_id,psystem_id) values (1,0,'localnode','LN',5,1);"
		"INSERT INTO nodes (id,urn_index,name,identifier,admin_id,psystem_id) values (2,0,'cooperatingnode','CN',6,1);"	
		"INSERT INTO cooperating_nodes (node_id,cooperating_node_id) values (1,2);"		
		"INSERT INTO nodes_contractors (node_id,contractor_user_id) VALUES (1,1);"
		"INSERT INTO nodes_contractors (node_id,contractor_user_id) VALUES (1,4);"
		"INSERT INTO nodes_contractors (node_id,contractor_user_id) VALUES (1,5);"
        "INSERT INTO conversion_routines (id,name,target_suffix,type,psystem_id) VALUES (1,'TIFF',null,'de.uzk.hki.da.convert.TiffConversionStrategy',1);"
        "INSERT INTO conversion_routines (id,name,target_suffix,type,psystem_id) VALUES (2,'PIMG','jpg','de.uzk.hki.da.convert.PublishImageConversionStrategy',1);"
        "INSERT INTO conversion_routines (id,name,target_suffix,type,params,psystem_id) VALUES (3,'CLITIF','tif','de.uzk.hki.da.convert.CLIConversionStrategy','convert input output',1);"
        "INSERT INTO conversion_routines (id,name,target_suffix,type,params,psystem_id) VALUES (4,'CLICOPY','*','de.uzk.hki.da.convert.PublishCLIConversionStrategy','cp input output',1);"
        "INSERT INTO conversion_policies (id,psystem_id,source_format,conversion_routine_id,presentation) VALUES (1,1,'fmt/353',1,false);"
        "INSERT INTO conversion_policies (id,psystem_id,source_format,conversion_routine_id,presentation) VALUES (2,1,'fmt/353',2,true);"
        "INSERT INTO conversion_policies (id,psystem_id,source_format,conversion_routine_id,presentation) VALUES (3,1,'fmt/116',3,false);"
        "INSERT INTO conversion_policies (id,psystem_id,source_format,conversion_routine_id,presentation) VALUES (4,1,'x-fmt/392',2,true);"
        "INSERT INTO conversion_policies (id,psystem_id,source_format,conversion_routine_id,presentation) VALUES (5,1,'fmt/4',2,true);"
        "INSERT INTO conversion_policies (id,psystem_id,source_format,conversion_routine_id,presentation) VALUES (6,1,'fmt/16',4,true);"
        "INSERT INTO conversion_policies (id,psystem_id,source_format,conversion_routine_id,presentation) VALUES (7,1,'fmt/354',4,true);"
        "INSERT INTO conversion_policies (id,psystem_id,source_format,conversion_routine_id,presentation) VALUES (8,1,'fmt/43',3,false);"
        "INSERT INTO conversion_policies (id,psystem_id,source_format,conversion_routine_id,presentation) VALUES (9,1,'fmt/43',2,true);"
        
        "INSERT INTO subformat_identification_strategy_puid_mappings (id,format_puid,subformat_identification_strategy_name) VALUES (1,'fmt/101','de.uzk.hki.da.format.XMLSubformatIdentifier');"
 	"INSERT INTO subformat_identification_strategy_puid_mappings (id,format_puid,subformat_identification_strategy_name) VALUES (2,'x-fmt/384','de.uzk.hki.da.format.FFmpegSubformatIdentifier');"
        "INSERT INTO subformat_identification_strategy_puid_mappings (id,format_puid,subformat_identification_strategy_name) VALUES (3,'fmt/200','de.uzk.hki.da.format.FFmpegSubformatIdentifier');"
        "INSERT INTO subformat_identification_strategy_puid_mappings (id,format_puid,subformat_identification_strategy_name) VALUES (4,'fmt/5','de.uzk.hki.da.format.FFmpegSubformatIdentifier');"
 	"INSERT INTO subformat_identification_strategy_puid_mappings (id,format_puid,subformat_identification_strategy_name) VALUES (5,'fmt/353','de.uzk.hki.da.format.ImageMagickSubformatIdentifier');"
	)
fi

for i in "${sqls[@]}"
do
	echo "$i"
	cd $HIER
	
	if [ "$2" = "ci" ]
	then
	    psql -U cb_usr -d CB -c "$i"
	fi
	if [ "$2" = "dev" ]
	then
	    java -jar ../3rdParty/hsqldb/lib/sqltool.jar --autoCommit --sql "$i" xdb 
	fi
done




