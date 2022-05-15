// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import { Theme } from '@mui/material/styles';

declare module '@mui/styles/defaultTheme' {
    // eslint-disable-next-line @typescript-eslint/no-empty-interface
    interface DefaultTheme extends Theme { }
}

const lightTheme = {
    themeName: 'light',
    palette: {
        secondary: {
            light: '#5f5fc4',
            dark: '#001064',
            main: '#3f51b5',
            contrastText: '#fff',
        },
        background: {
            default: '#fafafa',
        },
    },
    components: {
        MuiButtonBase: {
            defaultProps: {
                // disable ripple for perf reasons
                disableRipple: true,
            },
        },
        MuiAppBar: {
            styleOverrides: {
                colorSecondary: {
                    backgroundColor: '#80bf44',
                },
            },
        },
        MuiButton: {
            styleOverrides: {
                textPrimary: {
                    color: '#80bf44',
                },
                containedPrimary: {
                    backgroundColor: '#80bf44',
                },
            },
        },
        MuiFilledInput: {
            styleOverrides: {
                root: {
                    backgroundColor: 'rgba(0, 0, 0, 0.0) !important',
                },
            },
        },
        MuiSlider: {
            styleOverrides: {
                root: {
                    color: '#80bf44',
                },
                valueLabel: {
                    backgroundColor: '#80bf44',
                },
            },
        },
        MuiSwitch: {
            styleOverrides: {
                colorPrimary: {
                    '&$checked': {
                        color: '#80bf44',
                    },
                    '&$checked + $track': {
                        backgroundColor: '#80bf44',
                    },
                },
            },
        },
        MuiChip: {
            styleOverrides: {
                label: {
                    overflow: 'visible',
                },
            },
        },
        RaLogin: {
            styleOverrides: {
                root: {
                    backgroundImage: 'radial-gradient(circle at 50% 14em, #df8b84 0%, #f66b61 60%, #fb6e63 100%)',
                },
            },
        },
        RaTopToolbar: {
            styleOverrides: {
                root: {
                    paddingTop: '12px',
                },
            },
        },
        RaConfirm: {
            styleOverrides: {
                confirmPrimary: {
                    color: '#80bf44',
                },
            },
        },
        RaEmpty: {
            styleOverrides: {
                message: {
                    color: '#80bf44',
                },
            },
        },
    },
};

export default lightTheme;
