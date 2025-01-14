package com.example.designsystem.theme

import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.createRippleModifierNode
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorProducer
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.unit.Dp

/**
 * Custom ripple configuration for the application's design system.
 *
 * This provides a ripple with a custom color, while keeping all other behavior the same as the
 * default Material ripple.
 *
 * @param rippleColor The color of the ripple.
 */
@Immutable
class CustomRippleTheme(
    val rippleColor: Color
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CustomRippleTheme) return false

        return rippleColor == other.rippleColor
    }

    override fun hashCode(): Int {
        return rippleColor.hashCode()
    }
}

/**
 * Creates a custom ripple with a specified color for the application's design system.
 *
 * @param rippleTheme The CustomRippleTheme containing the custom ripple color.
 * @param bounded If true, ripples are clipped by the bounds of the target layout.
 * @param radius The radius of the ripple.
 */
@Composable
@Stable
fun customRipple(
    rippleTheme: CustomRippleTheme,
    bounded: Boolean = true,
    radius: Dp = Dp.Unspecified,
): IndicationNodeFactory {
    return rememberCustomRippleNodeFactory(rippleTheme, bounded, radius)
}

@Composable
fun rememberCustomRippleNodeFactory(
    rippleTheme: CustomRippleTheme,
    bounded: Boolean,
    radius: Dp
): IndicationNodeFactory {
    val colorProducer = remember {
        ColorProducer {
            rippleTheme.rippleColor
        }
    }
    return remember(rippleTheme, bounded, radius) {
        CustomRippleNodeFactory(bounded, radius, colorProducer)
    }
}

private class CustomRippleNodeFactory(
    private val bounded: Boolean,
    private val radius: Dp,
    private val colorProducer: ColorProducer
) : IndicationNodeFactory {
    override fun create(interactionSource: InteractionSource): DelegatableNode {
        return createRippleModifierNode(
            interactionSource = interactionSource,
            bounded = bounded,
            radius = radius,
            color = colorProducer,
            rippleAlpha = { CustomRippleDefaults.RippleAlpha }
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CustomRippleNodeFactory) return false
        if (bounded != other.bounded) return false
        if (radius != other.radius) return false
        if (colorProducer != other.colorProducer) return false
        return true
    }

    override fun hashCode(): Int {
        var result = bounded.hashCode()
        result = 31 * result + radius.hashCode()
        result = 31 * result + colorProducer.hashCode()
        return result
    }
}

/**
 * Default values for the custom ripple.
 */
object CustomRippleDefaults {
    /**
     * Default alpha values for the custom ripple in different states.
     */
    val RippleAlpha: RippleAlpha = RippleAlpha(
        pressedAlpha = 0.12f,
        focusedAlpha = 0.12f,
        draggedAlpha = 0.16f,
        hoveredAlpha = 0.08f
    )
}

// Function to apply the custom ripple as the default indication for the theme
@Composable
fun CustomRippleProvider(
    rippleTheme: CustomRippleTheme,
    content: @Composable () -> Unit
) {
    val rippleIndication = customRipple(rippleTheme)
    CompositionLocalProvider(
        LocalIndication provides rippleIndication,
        content = content
    )
}