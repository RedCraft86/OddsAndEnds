package com.redcraft86.oddsandends.mixin;

import net.minecraft.server.network.ServerGamePacketListenerImpl;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ServerGamePacketListenerImpl.class)
public class RemoveSpeedLimits {
    @ModifyConstant(method = "handleMovePlayer", constant = @Constant(floatValue = 100.0f))
    private float maxPlayerSpeed(float speed) { return Float.MAX_VALUE; }

    @ModifyConstant(method = "handleMovePlayer", constant = @Constant(floatValue = 300.0f))
    private float maxElytraSpeed(float speed) { return Float.MAX_VALUE; }

    @ModifyConstant(method = "handleMovePlayer", constant = @Constant(doubleValue = 0.0625))
    private double playerMovedWrong(double speed) { return Double.MAX_VALUE; }

    @ModifyConstant(method = "handleMoveVehicle", constant = @Constant(doubleValue  = 100.0f))
    private double maxVehicleSpeed(double speed) { return Double.MAX_VALUE; }

    @ModifyConstant(method = "handleMoveVehicle", constant = @Constant(doubleValue = 0.0625))
    private double vehicleMovedWrong(double speed) { return Double.MAX_VALUE; }
}
