package net.minecraft.src;

public class EntityDamageSourceIndirect extends EntityDamageSource
{
    private Entity indirectEntity;

    public EntityDamageSourceIndirect(String s, Entity entity, Entity entity1)
    {
        super(s, entity);
        indirectEntity = entity1;
    }

    public Entity getSourceOfDamage()
    {
        return damageSourceEntity;
    }

    public Entity getEntity()
    {
        return indirectEntity;
    }
}
