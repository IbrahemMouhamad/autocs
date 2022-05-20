// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React, { memo } from 'react';
import { BasicTooltip } from '@nivo/tooltip';

const LinePointTooltip = ({ point, xLegend }): JSX.Element => (
    <BasicTooltip
        id={
            (
                <span>
                    {xLegend}
                    :
                    {' '}
                    <strong>{point.data.xFormatted}</strong>
                    ,
                    {' '}
                    {point.serieId}
                    :
                    {' '}
                    <strong>{point.data.yFormatted}</strong>
                </span>
            )
        }
        enableChip
        color={point.serieColor}
    />
);

export default memo(LinePointTooltip);
