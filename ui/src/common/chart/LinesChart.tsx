// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import { ResponsiveLine, Serie } from '@nivo/line';

import LinePointTooltip from './LinePointTooltip';

interface Props {
    data: Serie[];
    xLegend?: string;
    yLegend?: string;
    yMax?: number;
    yMin?: number;
}

const LinesChart = ({
    data,
    xLegend,
    yLegend,
    yMax,
    yMin,
}: Props): JSX.Element => (
    <ResponsiveLine
        data={data}
        margin={{
            top: 50,
            right: 20,
            bottom: 50,
            left: 70,
        }}
        xScale={{ type: 'linear' }}
        yScale={{
            type: 'linear',
            min: yMin || 'auto',
            max: yMax,
            stacked: false,
            reverse: false,
        }}
        axisTop={null}
        axisRight={null}
        axisBottom={{
            tickSize: 3,
            tickPadding: 3,
            tickRotation: 0,
            legend: xLegend,
            legendOffset: 36,
            legendPosition: 'middle',
        }}
        axisLeft={{
            tickSize: 3,
            tickPadding: 3,
            tickRotation: 0,
            legend: yLegend,
            legendOffset: -60,
            legendPosition: 'middle',
        }}
        pointSize={1}
        pointColor={{ theme: 'background' }}
        pointBorderWidth={1}
        pointBorderColor={{ from: 'serieColor' }}
        pointLabelYOffset={-12}
        useMesh
        legends={[
            {
                anchor: 'top',
                direction: 'row',
                justify: false,
                translateX: 0,
                translateY: -40,
                itemsSpacing: 0,
                itemDirection: 'left-to-right',
                itemWidth: 120,
                itemHeight: 20,
                itemOpacity: 0.75,
                symbolSize: 12,
                symbolShape: 'circle',
                symbolBorderColor: 'rgba(0, 0, 0, .5)',
                effects: [
                    {
                        on: 'hover',
                        style: {
                            itemBackground: 'rgba(0, 0, 0, .03)',
                            itemOpacity: 1,
                        },
                    },
                ],
            },
        ]}
        tooltip={({ point }) => (<LinePointTooltip point={point} xLegend={xLegend} />)}
    />
);

export default LinesChart;
