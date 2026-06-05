import { beforeEach, describe, expect, it, vi } from 'vitest';

import axios from 'axios';
import dayjs from 'dayjs';

import { DATE_FORMAT } from '@/shared/composables/date-format';
import { Evaluation } from '@/shared/model/evaluation.model';

import EvaluationService from './evaluation.service';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: vi.spyOn(axios, 'get'),
  post: vi.spyOn(axios, 'post'),
  put: vi.spyOn(axios, 'put'),
  patch: vi.spyOn(axios, 'patch'),
  delete: vi.spyOn(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('Evaluation Service', () => {
    let service: EvaluationService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new EvaluationService();
      currentDate = new Date();
      elemDefault = new Evaluation(
        123,
        'AAAAAAA',
        currentDate,
        'AAAAAAA',
        'NORMAL',
        'PASS',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        false,
      );
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = { evaluationDate: dayjs(currentDate).format(DATE_FORMAT), ...elemDefault };
        axiosStub.get.mockResolvedValue({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.mockRejectedValue(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a Evaluation', async () => {
        const returnedFromService = { id: 123, evaluationDate: dayjs(currentDate).format(DATE_FORMAT), ...elemDefault };
        const expected = { evaluationDate: currentDate, ...returnedFromService };

        axiosStub.post.mockResolvedValue({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a Evaluation', async () => {
        axiosStub.post.mockRejectedValue(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a Evaluation', async () => {
        const returnedFromService = {
          evaluationName: 'BBBBBB',
          evaluationDate: dayjs(currentDate).format(DATE_FORMAT),
          periodLabel: 'BBBBBB',
          progressStatus: 'BBBBBB',
          result: 'BBBBBB',
          strengths: 'BBBBBB',
          weaknesses: 'BBBBBB',
          supportNeeded: 'BBBBBB',
          nextTrainingFocus: 'BBBBBB',
          positionAdjustmentNeeded: true,
          ...elemDefault,
        };

        const expected = { evaluationDate: currentDate, ...returnedFromService };
        axiosStub.put.mockResolvedValue({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a Evaluation', async () => {
        axiosStub.put.mockRejectedValue(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a Evaluation', async () => {
        const patchObject = {
          evaluationName: 'BBBBBB',
          periodLabel: 'BBBBBB',
          progressStatus: 'BBBBBB',
          weaknesses: 'BBBBBB',
          supportNeeded: 'BBBBBB',
          nextTrainingFocus: 'BBBBBB',
          positionAdjustmentNeeded: true,
          ...new Evaluation(),
        };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { evaluationDate: currentDate, ...returnedFromService };
        axiosStub.patch.mockResolvedValue({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a Evaluation', async () => {
        axiosStub.patch.mockRejectedValue(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of Evaluation', async () => {
        const returnedFromService = {
          evaluationName: 'BBBBBB',
          evaluationDate: dayjs(currentDate).format(DATE_FORMAT),
          periodLabel: 'BBBBBB',
          progressStatus: 'BBBBBB',
          result: 'BBBBBB',
          strengths: 'BBBBBB',
          weaknesses: 'BBBBBB',
          supportNeeded: 'BBBBBB',
          nextTrainingFocus: 'BBBBBB',
          positionAdjustmentNeeded: true,
          ...elemDefault,
        };
        const expected = { evaluationDate: currentDate, ...returnedFromService };
        axiosStub.get.mockResolvedValue([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of Evaluation', async () => {
        axiosStub.get.mockRejectedValue(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a Evaluation', async () => {
        axiosStub.delete.mockResolvedValue({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a Evaluation', async () => {
        axiosStub.delete.mockRejectedValue(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
