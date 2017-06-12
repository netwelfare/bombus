import java.util.ArrayList;
import java.util.Collection;

import cn.bombus.core.sql.type.TypeAliasRegistry;

public class Text
{

	public Text()
	{

	}

	public static void main(String[] args)
	{
		TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();
		typeAliasRegistry.registerAliases("entity");
		Boolean rest = Collection.class.isAssignableFrom(ArrayList.class);
		System.out.println(rest);
	}

}
